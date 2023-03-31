package com.jiamian.translation.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.jiamian.translation.common.exception.BOException;
import com.jiamian.translation.common.exception.ErrorMsg;
import com.jiamian.translation.common.service.LxtService;
import com.jiamian.translation.dao.redis.UserRedisService;
import com.jiamian.translation.dao.repository.UserInfoRepository;
import com.jiamian.translation.entity.dto.UserInfoDTO;
import com.jiamian.translation.entity.response.LoginUserResponse;
import com.jiamian.translation.model.Users;
import com.jiamian.translation.redis.RedisDao;
import com.jiamian.translation.util.AESUtils;
import com.jiamian.translation.util.UserTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jiamian.translation.common.entity.dto.ShortMessage;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @ClassName: UserServiceImpl
 * @Auther: z1115
 * @Date: 2023/3/26 10:59
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@Service
public class UserServiceImpl {
	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Value("${lxt.register_code_expire:300}")
	private String REGISTER_CODER_EXPIRE;

	@Value("${translation.user.avatar.url}")
	private String avatarUrl;

	@Autowired
	private RedisDao redisDao;

	@Autowired
	private UserRedisService userRedisService;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private LxtService lxtService;

	private static String REGISTER_SMS_PREFIX = "register_code:";

	private static String REGISTER_CODE_LIMIT_KEY = "register_code_limit";

	/**
	 * 短信验证码注册或登录
	 *
	 * @param
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public LoginUserResponse register(String phoneNumber, String phoneAreaCode,
			String verificationCode, String passWd, String newPassWd) {
		LoginUserResponse loginUserResponse = new LoginUserResponse();
		loginUserResponse.setRegisterStat(false);
		this.checkPassWord(passWd, newPassWd);
		String decryptPhoneNumber = AESUtils.decrypt(phoneNumber);
		// 验证短信验证码
		this.validRegisterCode(phoneAreaCode, decryptPhoneNumber,
				verificationCode);
		// 用户已经存在 则登录 否则注册
		Optional<Users> appUserInfoOptional = userInfoRepository
				.findByPhoneNumber(phoneNumber);
		Users userInfo = appUserInfoOptional.orElseGet(() -> {
			Users appUserInfo = new Users();
			Long incrUserId = userRedisService.incrUserId();
			appUserInfo.setUserId(incrUserId);
			appUserInfo.setAvatarUrl(avatarUrl);
			appUserInfo.setPhoneNumber(phoneNumber);
			String nickName = RandomUtil.randomString(5);
			appUserInfo.setUserName("model_" + nickName);
			appUserInfo.setGender(0);
			appUserInfo.setPassWord(passWd);
			loginUserResponse.setRegisterStat(true);
			return appUserInfo;
		});
		userInfo.setPassWord(passWd);
		userInfo = userInfoRepository.save(userInfo);
		BeanUtil.copyProperties(userInfo, loginUserResponse);
		loginUserResponse.setToken(
				UserTokenUtil.generateToken(loginUserResponse.getUserId()));
		return loginUserResponse;

	}

	private void countMaxSendNumber(String phoneNumber, String ipStr) {
		double hincr = redisDao.hincr(REGISTER_CODE_LIMIT_KEY, phoneNumber, 1);

		double ipLimit = redisDao.hincr("tl:sms:ip:limit", ipStr, 1);
		if (ipLimit > 5) {
			throw new BOException("同一个ip每天最大收取验证码次数为5次哦~");
		} else {
			// 验证码次数凌晨清除
			Duration duration = Duration.between(LocalDateTime.now(),
					LocalDate.now().plusDays(1).atTime(0, 0, 0));
			redisDao.expire("tl:sms:ip:limit", duration.getSeconds());
		}
		if (hincr > 5) {
			throw new BOException("每天最大收取验证码次数为5次哦~");
		} else {
			// 验证码次数凌晨清除
			Duration duration = Duration.between(LocalDateTime.now(),
					LocalDate.now().plusDays(1).atTime(0, 0, 0));
			redisDao.expire(REGISTER_CODE_LIMIT_KEY, duration.getSeconds());
		}
	}

	public String getCode(String phoneNumber, String countryCode,
			String ipStr) {
		// 真实手机号
		String registerCode = "";
		boolean codeSendResult = false;
		// Step2 : 统计每天最大获取短信次数, 超过最大次数, 抛出异常提示
		this.countMaxSendNumber(countryCode + phoneNumber, ipStr);
		// Step1: 生成验证码
		String pushCode = RandomStringUtils.randomNumeric(4);
		if ("86".equals(countryCode)) {
			codeSendResult = lxtService
					.sendMessage(new ShortMessage(phoneNumber, "", pushCode));
		}
		logger.info("pushCode: " + pushCode);
		// 5. 发送成功,保存注册码,待验证
		if (codeSendResult) {
			redisDao.set(REGISTER_SMS_PREFIX + countryCode + phoneNumber,
					pushCode, Long.parseLong(REGISTER_CODER_EXPIRE));
			registerCode = pushCode;
		} else {
			// 6. 发送失败, 抛出发送失败的异常提示
			throw new BOException(ErrorMsg.SHORT_MESSAGE_ERROR);
		}
		return registerCode;
	}

	/**
	 * 效验短信验证码 redis存的是解密后的手机号码
	 *
	 * @param countryCode
	 * @param phoneNumber
	 * @param registerCode
	 * @return
	 */
	public boolean validRegisterCode(String countryCode, String phoneNumber,
			String registerCode) {
		// 缓存获取 并 验证
		Object redisCode = redisDao
				.get(REGISTER_SMS_PREFIX + countryCode + phoneNumber);
		// 如果验证码 在redis中不存在 或者 验证码 对比失败
		// 抛出异常提示: 短信验证码填写错误或超时!
		if (redisCode == null
				|| !registerCode.equalsIgnoreCase((String) redisCode)) {
			throw new BOException(ErrorMsg.SHORT_MESSAGE_VALID_ERROR);
		}
		// 验证成功 ,删除注册码
		redisDao.del(REGISTER_SMS_PREFIX + countryCode + phoneNumber);
		return true;
	}

	/**
	 * 获取用户信息
	 *
	 * @param userId
	 * @return
	 */
	public UserInfoDTO getUserInfoByUserId(Long userId) {
		// 如果 目标用户为空 赋值目标用户为登录用户
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		try {
			Users userInfo = userInfoRepository.findByUserId(userId)
					.orElseThrow(() -> {
						throw new BOException(ErrorMsg.USER_NOT_FOUND_ERROR);
					});
			BeanUtil.copyProperties(userInfo, userInfoDTO);
			return userInfoDTO;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return userInfoDTO;
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateUser(String userName, Integer gender, String avatarUrl,
			Long userId) {
		// this.validParam(userName, gender, avatarUrl);
		Optional<Users> optionalAppUserInfo = userInfoRepository
				.findByUserId(userId);
		if (optionalAppUserInfo.isPresent()) {
			Users userInfo = optionalAppUserInfo.get();
			userInfo.setUserName(userName);
			userInfo.setAvatarUrl(avatarUrl);
			userInfo.setGender(gender);
			userInfoRepository.save(userInfo);
		} else {
			throw new BOException(ErrorMsg.USER_NOT_FOUND_ERROR);
		}
	}

	/**
	 * 登录
	 * 
	 * @param phoneNumber
	 * @param passWord
	 * @return
	 */
	public LoginUserResponse login(String phoneNumber, String passWord) {
		LoginUserResponse loginUserResponse = new LoginUserResponse();
		Optional<Users> optionalUsers = userInfoRepository
				.findByPhoneNumber(phoneNumber);
		if (optionalUsers.isPresent()) {
			Users users = optionalUsers.get();
			if (!users.getPassWord().equals(passWord)) {
				throw new BOException(ErrorMsg.EMAIL_OR_PASSWD_ERROR);
			}
			BeanUtil.copyProperties(users, loginUserResponse);
			loginUserResponse.setToken(
					UserTokenUtil.generateToken(loginUserResponse.getUserId()));
			loginUserResponse.setRegisterStat(false);
		} else {
			throw new BOException(ErrorMsg.USER_NOT_FOUND_ERROR);
		}
		return loginUserResponse;
	}

	// private void validParam(String userName, Integer gender, String
	// avatarUrl) {
	// MachineCheckResult userNameResult = YiDunApi.checkText(userName);
	// if (MachineCheckResultEnum.PASS.getCode().intValue() != userNameResult
	// .getRsEnum().getCode()) {
	// throw new BOException(ErrorMsg.USER_INFO_EDIT_ERROR);
	// }
	// MachineCheckResult avatarUrlResult = YiDunApi.checkImage(avatarUrl);
	// if (MachineCheckResultEnum.PASS.getCode().intValue() != avatarUrlResult
	// .getRsEnum().getCode()) {
	// throw new BOException(ErrorMsg.USER_INFO_EDIT_ERROR);
	// }
	// if (gender == null) {
	// throw new BOException(ErrorMsg.USER_INFO_EDIT_ERROR);
	// }
	// }

	private void checkPassWord(String newPasswd, String replyNewPasswd) {
		if (AESUtils.decrypt(newPasswd).length() < 6) {
			throw new BOException(ErrorMsg.PASS_WORD_SHORT_ERROR);
		}
		if (!newPasswd.equals(replyNewPasswd)) {
			throw new BOException(ErrorMsg.REPLY_PASS_WORD_ERROR);
		}
	}
}
