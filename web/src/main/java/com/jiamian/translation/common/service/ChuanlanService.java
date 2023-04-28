package com.jiamian.translation.common.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jiamian.translation.entity.dto.ShortMessage;
import com.jiamian.translation.util.LoggerUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ding
 * @date 2020/3/24
 */
@Service
public class ChuanlanService {

	@Value("${chuanlan.sms.url}")
	private String BASE_URL;

	@Value("${chuanlan.account}")
	private String un;

	@Value("${chuanlan.password}")
	private String pw;
	@Autowired
	private RestTemplate restTemplate;

	public boolean send(ShortMessage shortMessage) {
		boolean success = false;
		try {
			String content = "【炼丹阁】验证码：{content}，有效期为5分钟。若非本人发送，请注意您的账号安全。"
					.replace("{content}", shortMessage.getContent());
			JSONObject map = new JSONObject();
			map.put("account", un);// API账号
			map.put("password", pw);// API密码
			map.put("msg", content);// 短信内容
			map.put("phone", shortMessage.getMobileNo());// 手机号
			map.put("report", "false");// 是否需要状态报告
			String result = HttpUtil.post(BASE_URL, map.toJSONString());
			LoggerUtil.info(" === 给号码:{} , 发送短信结果:{} === ",
					shortMessage.getMobileNo(), result);
			JSONObject jsonObject = JSONObject.parseObject(result);
			Integer code = jsonObject.getInteger("code");
			String messageId = jsonObject.getString("msgId");
			if (code != 0) {
				String msg = String.format("向手机号[%s]发送短信息[%s]失败 ",
						shortMessage.getMobileNo(), shortMessage.getContent());
				LoggerUtil.error(msg);

			} else {
				LoggerUtil
						.info(String.format("向手机号[%s]发送短信息[%s]成功. MessageId为%s",
								shortMessage.getMobileNo(),
								shortMessage.getContent(), messageId));
				success = true;
			}
		} catch (Exception e) {
			String msg = String.format("向手机号[%s]发送短信息[%s]失败.",
					shortMessage.getMobileNo(), shortMessage.getContent());
			LoggerUtil.error(msg, e);
			StackTraceElement st = e.getStackTrace()[0];
			LoggerUtil.error("=== 异常,异常类名:{},方法名:{},行号:{} ===",
					st.getClassName(), st.getMethodName(), st.getLineNumber());
		}
		return success;
	}

}
