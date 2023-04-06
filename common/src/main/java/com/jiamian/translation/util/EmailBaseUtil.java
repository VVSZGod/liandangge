package com.jiamian.translation.util;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Properties;

public class EmailBaseUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(EmailBaseUtil.class);

	// 一般的通知接收
	public static final String RECEIVER = "untest.mp@jiamiantech.com";

	// 系统异常报警接收
	public static final String BAOJING_RECEIVER = "untest.mp@jiamiantech.com";

	public static void sendEmail(String title, String content) {
		sendEmail(title, content, RECEIVER);
	}

	public static void sendBaojingEmail(String title, String content) {
		sendEmail(title, content, BAOJING_RECEIVER);
	}

	// TODO 邮件重新实现
	/**
	 * 邮件提醒管理员有新留言
	 *
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @param email
	 *            收件人邮箱
	 */
	public static void sendEmail(String title, String content, String email) {
		try {
			logger.info("sendEmail, title = {}, content = {}, email = {}",
					title, content, email);
			// 配置发送邮件的环境属性
			final Properties props = new Properties();
			// 表示SMTP发送邮件，需要进行身份验证
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", EmailAccount.ADMIN.getHost());
			props.put("mail.transport.protocol", "smtp");
			// SSLSocketFactory类的端口
			props.put("mail.smtp.socketFactory.port", "465");
			// SSLSocketFactory类
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			// ssl加密端口
			props.put("mail.smtp.port", "465");
			// 发件人email账号
			props.put("mail.user", EmailAccount.ADMIN.getUsername());
			// 访问SMTP服务时需要提供的密码
			props.put("mail.password", EmailAccount.ADMIN.getPassword());
			// 构建授权信息，用于进行SMTP进行身份验证
			Authenticator authenticator = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// 用户名、密码
					String userName = props.getProperty("mail.user");
					String password = props.getProperty("mail.password");
					return new PasswordAuthentication(userName, password);
				}
			};
			// 使用环境属性和授权信息，创建邮件会话
			Session mailSession = Session.getInstance(props, authenticator);
			// 创建邮件消息
			MimeMessage message = new MimeMessage(mailSession);
			// 设置发件人
			InternetAddress form = new InternetAddress(
					props.getProperty("mail.user"));
			message.setFrom(form);
			// 设置收件人
			InternetAddress to = new InternetAddress(email);
			message.setRecipient(RecipientType.TO, to);
			// 设置邮件标题
			message.setSubject(MimeUtility.encodeText(title, "UTF-8", "Q"));
			// 设置邮件的内容体
			message.setContent(StrUtil.isBlank(content) ? title : content,
					"text/html;charset=UTF-8");
			// 发送邮件
			Transport.send(message);
		} catch (Exception e) {
			logger.error(String.format("邮件发送失败,标题:%s,内容:%s,邮箱:%s", title,
					content, email), e);
		}

	}

	/**
	 * 邮件账户
	 */
	public enum EmailAccount {

		/**
		 * 系统
		 */
		ADMIN("smtp.exmail.qq.com", "untest.mp@jiamiantech.com", "Rushu790415");

		private String host;

		private String username;

		private String password;

		EmailAccount(String host, String username, String password) {
			this.host = host;
			this.username = username;
			this.password = password;
		}

		public String getHost() {
			return host;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}
	}
}
