package com.jiamian.translation.controller;

import cn.hutool.core.util.ObjectUtil;
import com.jiamian.translation.annotation.LoginUser;
import com.jiamian.translation.common.entity.JsonResult;
import com.jiamian.translation.common.exception.BOException;
import com.jiamian.translation.common.exception.ErrorMsg;
import com.jiamian.translation.component.HttpDao;
import com.jiamian.translation.entity.dto.UserInfoDTO;
import com.jiamian.translation.entity.response.LoginUserResponse;
import com.jiamian.translation.entity.response.UserResponse;
import com.jiamian.translation.request.LoginRequest;
import com.jiamian.translation.service.UserServiceImpl;
import com.jiamian.translation.util.AESUtils;
import com.jiamian.translation.util.UserTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author DingGuangHui
 * @date 2023/2/13
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private HttpDao httpDao;

	// /push/code 发送短信
	@PostMapping("/push/code")
	@ApiOperation("发送验证码")
	public JsonResult getCode(@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam(value = "areaCode", defaultValue = "86") String areaCode) {
		if (StringUtils.isEmpty(phoneNumber)) {
			throw new BOException(ErrorMsg.PARAMETER_ERROR);
		}
		phoneNumber = AESUtils.decrypt(phoneNumber);
		String ipAddr = httpDao.getIpAddr();
		userService.getCode(phoneNumber, areaCode, ipAddr);
		return JsonResult.succResult();
	}

	/**
	 * 注册/登录
	 *
	 * @return
	 */
	@PostMapping("/login")
	@ApiOperation("登录")
	public JsonResult<LoginUserResponse> login(
			@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("passWord") String passWord) {
		LoginUserResponse loginUserResponse = userService.login(phoneNumber,
				passWord);
		return JsonResult.succResult(loginUserResponse);
	}

	@PostMapping("/register")
	@ApiOperation("注册")
	public JsonResult<LoginUserResponse> register(
			@RequestBody LoginRequest loginRequest) {
		String verificationCode = loginRequest.getVerificationCode();
		String phoneNumber = loginRequest.getPhoneNumber();
		String phoneAreaCode = loginRequest.getPhoneAreaCode();
		String passWd = loginRequest.getPassWd();
		String newPassWd = loginRequest.getNewPassWd();
		LoginUserResponse loginUserResponse = userService.register(
				phoneNumber, phoneAreaCode, verificationCode, passWd,
				newPassWd);
		return JsonResult.succResult(loginUserResponse);
	}

	// /info 查看用户信息
	@GetMapping("/info")
	@ApiOperation("用户信息")
	public JsonResult<UserResponse> getUserInfoByUserId(
			@LoginUser Long userId) {
		UserTokenUtil.needLogin(userId);
		UserInfoDTO appUserInfoDTO = userService.getUserInfoByUserId(userId);
		UserResponse userResponse = new UserResponse();
		userResponse.setUserInfoDTO(appUserInfoDTO);
		return JsonResult.succResult(userResponse);
	}
}
