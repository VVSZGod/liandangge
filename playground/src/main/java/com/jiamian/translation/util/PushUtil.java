package com.jiamian.translation.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushUtil {

    private static final Logger logger = LoggerFactory.getLogger(PushUtil.class);

    public static void info(String title) {
        info(title, null, null);
    }

    public static void info(String title, String desc) {
        info(title, desc, null);
    }

    public static void info(String title, String desc, String email) {
        try {
            if (StringUtils.contains(desc, "AutoTest")) {
                return;
            }
            ThreadUtil.getExecutorPool().submit(() -> {
                try {
                    if (StringUtils.isBlank(email)) {
                        EmailBaseUtil.sendEmail(title, desc);
                    } else {
                        EmailBaseUtil.sendEmail(title, desc, email);
                    }
                } catch (Exception e) {
                    logger.error("PushUtil info fail, title:{}, desc:{}", title, desc, e);
                }
            });
        } catch (Exception e) {
            logger.error("PushUtil info fail, thread fail, title:{}, desc:{}", title, desc, e);
        }
    }

    public static void warn(String title) {
        warn(title, null);
    }

    public static void warn(String title, String desc) {
        try {
            ThreadUtil.getExecutorPool().submit(() -> {
                try {
                    EmailBaseUtil.sendBaojingEmail(title, desc);
                } catch (Exception e) {
                    logger.error("PushUtil warn fail, title:{}, desc:{}", title, desc, e);
                }
            });
        } catch (Exception e) {
            logger.error("PushUtil warn fail, thread fail, title:{}, desc:{}", title, desc, e);
        }
    }

    public static void warnAll(String title, String desc) {
        try {
            ThreadUtil.getExecutorPool().submit(() -> {
                try {
                    EmailBaseUtil.sendBaojingEmail(title, desc);
                } catch (Exception e) {
                    logger.error("PushUtil warnAll fail, title:{}, desc:{}", title, desc, e);
                }
            });
        } catch (Exception e) {
            logger.error("PushUtil warnAll fail, thread fail, title:{}, desc:{}", title, desc, e);
        }
    }
}
