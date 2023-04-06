package com.jiamian.translation.util;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: ClientConfig
 * @Auther: z1115
 * @Date: 2023/3/25 19:53
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class ClientConfig {
    private int connectTimeout = 3;
    private int writeTimeout = 5;
    private int readTimeOut = 5;
    private int callTimeout = 10;
    private int maxIdleConnections;
    private int keepAliveTime;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public int getCallTimeout() {
        return callTimeout;
    }

    public void setCallTimeout(int callTimeout) {
        this.callTimeout = callTimeout;
    }

    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public void setMaxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
