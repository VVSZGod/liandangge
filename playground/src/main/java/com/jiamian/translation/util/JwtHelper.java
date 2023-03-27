package com.jiamian.translation.util;

import java.util.*;

import com.jiamian.translation.common.config.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import cn.hutool.core.util.StrUtil;

public class JwtHelper {
	private static Logger logger = LoggerFactory.getLogger(JwtHelper.class);

//	// 秘钥
	// 签名是有谁生成
	private static final String ISSUSER = "translate";
	// 签名的主题
	private static final String SUBJECT = "daily up up up";
	// 签名的观众
	private static final String AUDIENCE = "engrave_77";

	public static String createToken(Long userId) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SystemConfig.getSecret());
			Map<String, Object> map = new HashMap<String, Object>();
			Date nowDate = new Date();
			// 过期时间：2天
			Date expireDate = getAfterDate(nowDate, 0, 0, 2, 0, 0, 0);
			map.put("alg", "HS256");
			map.put("typ", "JWT");
			String token = JWT.create()
					// 设置头部信息 Header
					.withHeader(map)
					// 设置 载荷 Payload
					.withClaim("userId", userId).withIssuer(ISSUSER)
					.withSubject(SUBJECT).withAudience(AUDIENCE)
					// 生成签名的时间
					.withIssuedAt(nowDate)
					// 签名过期的时间
					.withExpiresAt(expireDate)
					// 签名 Signature
					.sign(algorithm);
			return token;
		} catch (JWTCreationException exception) {
			exception.printStackTrace();
			logger.info("生成token 异常，userId:{}，ex：{}", userId, exception);
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			Algorithm algorithm = Algorithm.HMAC256("dev");
			Map<String, Object> map = new HashMap<String, Object>();
			Date nowDate = new Date();
			// 过期时间：2天
			Date expireDate = getAfterDate(nowDate, 0, 0, 2, 0, 0, 0);
			map.put("alg", "HS256");
			map.put("typ", "JWT");
			String token = JWT.create()
					// 设置头部信息 Header
					.withHeader(map)
					// 设置 载荷 Payload
					.withClaim("userId", 100001).withIssuer(ISSUSER)
					.withSubject(SUBJECT).withAudience(AUDIENCE)
					// 生成签名的时间
					.withIssuedAt(nowDate)
					// 签名过期的时间
					.withExpiresAt(expireDate)
					// 签名 Signature
					.sign(algorithm);
			System.out.println(token);
		} catch (JWTCreationException exception) {
			exception.printStackTrace();
			logger.info("生成token 异常，userId:{}，ex：{}", 1, exception);
		}
	}
	public static Long verifyTokenAndGetUserId(String token) {
		try {
			if (StrUtil.isNotBlank(token)) {
				Algorithm algorithm = Algorithm.HMAC256(SystemConfig.getSecret());
				JWTVerifier verifier = JWT.require(algorithm)
						.withIssuer(ISSUSER).build();
				DecodedJWT jwt = verifier.verify(token);
				Map<String, Claim> claims = jwt.getClaims();
				Claim claim = claims.get("userId");
				return claim.asLong();
			} else {
				return 0L;
			}
		} catch (Exception exception) {
			logger.error("解析token异常:{},{}", token, exception);
		}
		return 0L;
	}

	public static Date getAfterDate(Date date, int year, int month, int day,
			int hour, int minute, int second) {
		if (date == null) {
			date = new Date();
		}

		Calendar cal = new GregorianCalendar();

		cal.setTime(date);
		if (year != 0) {
			cal.add(Calendar.YEAR, year);
		}
		if (month != 0) {
			cal.add(Calendar.MONTH, month);
		}
		if (day != 0) {
			cal.add(Calendar.DATE, day);
		}
		if (hour != 0) {
			cal.add(Calendar.HOUR_OF_DAY, hour);
		}
		if (minute != 0) {
			cal.add(Calendar.MINUTE, minute);
		}
		if (second != 0) {
			cal.add(Calendar.SECOND, second);
		}
		return cal.getTime();
	}

}
