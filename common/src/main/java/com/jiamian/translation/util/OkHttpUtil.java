package com.jiamian.translation.util;

import cn.hutool.http.ssl.TrustAnyHostnameVerifier;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * okhttp连接池单例
 *
 * @author duanzm
 * @create 2018-10-11 16:15
 **/
public class OkHttpUtil {
    private OkHttpUtil() {
    }

    public static OkHttpClient getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    public static String getString(String articleUrl, Map<String, String> wxHeaderMap) {
        return null;
    }

    public static OkHttpClient createClient(ClientConfig config) {
        OkHttpClient.Builder okHttpClientBuilder = createClientBuilder(config);
        return okHttpClientBuilder.build();
    }

    private static OkHttpClient.Builder createClientBuilder(
            ClientConfig config) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(config.getConnectTimeout(),
                TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(config.getWriteTimeout(),
                TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(config.getReadTimeOut(),
                TimeUnit.SECONDS);
        // 连接池配置
        if (config.getMaxIdleConnections() > 0 && config.getKeepAliveTime() > 0) {
            okHttpClientBuilder.connectionPool(
                    new ConnectionPool(config.getMaxIdleConnections(),
                            config.getKeepAliveTime(), TimeUnit.SECONDS));
        }
        // 添加https支持
        okHttpClientBuilder.hostnameVerifier(new TrustAnyHostnameVerifier());
        X509TrustManager trustManager = new TrustAnyX509TrustManager();
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager},
                    new java.security.SecureRandom());
            okHttpClientBuilder.sslSocketFactory(sslContext.getSocketFactory(),
                    trustManager);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return okHttpClientBuilder;
    }

    /**
     * client配置,时间单位 秒
     */

    private static enum Singleton {
        /**
         *
         */
        INSTANCE;
        private OkHttpClient singleton;

        private Singleton() {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(6L, TimeUnit.SECONDS);
            builder.readTimeout(6L, TimeUnit.SECONDS);
            builder.writeTimeout(6L, TimeUnit.SECONDS);
            ConnectionPool connectionPool = new ConnectionPool(50, 60, TimeUnit.SECONDS);
            builder.connectionPool(connectionPool);
            singleton = builder.build();
        }

        public OkHttpClient getInstance() {
            return singleton;
        }
    }


    /**
     * token置换手机号后台接口
     */
    public static JSONObject postRequest(String url, Map<String, String> params) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> m : params.entrySet()) {
                builder.add(m.getKey(), m.getValue());
            }
            RequestBody body = builder.build();
            Request request = new Request.Builder().post(body).url(url).build();
            Response response = OkHttpUtil.getInstance().newCall(request).execute();
            if (response.isSuccessful()) {
                String content = response.body().string();
                if (StringUtils.isNotBlank(content)) {
                    return JSONObject.parseObject(content);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONObject post(String url, Map<String, String> params) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            if (params != null) {
                for (Map.Entry<String, String> m : params.entrySet()) {
                    builder.add(m.getKey(), m.getValue());
                }
            }
            RequestBody body = builder.build();
            Request request = new Request.Builder().post(body).url(url).build();
            Response response = OkHttpUtil.getInstance().newCall(request).execute();
            if (response.isSuccessful()) {
                String content = response.body().string();
                //System.out.println("response:"+content);
                if (StringUtils.isNotBlank(content)) {
                    JSONObject jsonObject = JSONObject.parseObject(content);
                    return jsonObject;
                }
            } else {
                LoggerUtil.info(url + "--" + response.code() + "--" + response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static JSONObject get(String url, Map<String, String> params) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            if (params != null) {
                for (Map.Entry<String, String> m : params.entrySet()) {
                    builder.add(m.getKey(), m.getValue());
                }
            }

            Request request = new Request.Builder().get().url(url).build();
            Response response = OkHttpUtil.getInstance().newCall(request).execute();
            if (response.isSuccessful()) {
                String content = response.body().string();
                //System.out.println("response:"+content);
                if (StringUtils.isNotBlank(content)) {
                    JSONObject jsonObject = JSONObject.parseObject(content);
                    return jsonObject;
                }
            } else {
                LoggerUtil.info(url + "--" + response.code() + "--" + response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
