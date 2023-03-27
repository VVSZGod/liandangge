package com.jiamian.translation.util;


import com.jiamian.translation.common.entity.JsonResult;
import com.jiamian.translation.common.exception.ErrorCodeEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

    public static JsonResult handlerRedisException(Object object, Exception e) {
        ErrorCodeEnum errorCodeEnum = ErrorCodeEnum.getByClass(e.getClass());
        if (errorCodeEnum.isShowError()) {
            return JsonResult.errorResult(StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : errorCodeEnum.getMsg(), errorCodeEnum);
        } else {
            return JsonResult.errorResult(errorCodeEnum.getMsg(), errorCodeEnum);
        }
    }

    /**
     * 获取完整的堆栈信息
     *
     * @param e 异常
     * @return 堆栈信息
     */
    public static String getStackTrace(Exception e) {
        if (e == null) {
            return null;
        }
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
    }
}
