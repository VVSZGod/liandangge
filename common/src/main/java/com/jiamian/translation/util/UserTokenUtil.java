package com.jiamian.translation.util;

import com.jiamian.translation.exception.AuthorizationException;

/**
 * 维护用户token
 */
public class UserTokenUtil {
	public static boolean isLogin(Long userId) {
		if (userId == null || userId == 0) {
			return false;
		}
		return true;
	}

	public static long needLogin(Long userId) {
		if (userId == null || userId == 0) {
			throw new AuthorizationException();
		}
		return userId;
	}

	public static long createUserId(Long userId) {
		if (userId == null || userId == 0) {
			return 0L;
		}
		return userId;
	}

	public static String generateToken(Long id,String systemSecret) {
		return JwtHelper.createToken(id,systemSecret);
	}

	public static Long getUserId(String token,String secret) {
		return JwtHelper.verifyTokenAndGetUserId(token,secret);
	}
}
