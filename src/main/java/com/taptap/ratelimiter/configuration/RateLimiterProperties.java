package com.taptap.ratelimiter.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kl (http://kailing.pub)
 * @since 2021/3/16
 */
@ConfigurationProperties(prefix = RateLimiterProperties.PREFIX)
public class RateLimiterProperties {

    public static final String PREFIX = "spring.ratelimiter";

    private int statusCode = 429;
    private String responseBody = "{\"code\":429,\"msg\":\"Too Many Requests\"}";

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

}
