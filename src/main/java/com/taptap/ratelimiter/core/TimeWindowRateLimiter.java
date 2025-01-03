package com.taptap.ratelimiter.core;

import com.taptap.ratelimiter.model.LuaScript;
import com.taptap.ratelimiter.model.Result;
import com.taptap.ratelimiter.model.Rule;
import org.redisson.api.RScript;
import org.redisson.api.RScript.ReturnType;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;

import java.util.Collections;
import java.util.List;


/**
 * @author kl (http://kailing.pub)
 * @since 2022/8/23
 */
public class TimeWindowRateLimiter implements RateLimiter {

    private final RScript rScript;

    public TimeWindowRateLimiter(RedissonClient client) {
        this.rScript = client.getScript(LongCodec.INSTANCE);
    }

    @Override
    public Result isAllowed(Rule rule) {
        List<Object> keys = getKeys(rule.getKey());
        String script = LuaScript.getTimeWindowRateLimiterScript();
        List<Long> results = rScript.eval(RScript.Mode.READ_WRITE, script, RScript.ReturnType.MULTI, keys, rule.getRate(), rule.getRateInterval());
        boolean isAllowed = results.get(0) == 1L;
        long ttl = results.get(1);

        return new Result(isAllowed, ttl);
    }

    @Override
    public void revoke(Rule rule) {
        List<Object> keys = getKeys(rule.getKey());
        String script = LuaScript.getTimeWindowRateLimiterCancelScript();
        rScript.eval(RScript.Mode.READ_WRITE, script, ReturnType.BOOLEAN, keys, rule.getRate(), rule.getRateInterval());
    }

    static List<Object> getKeys(String key) {
        String prefix = "request_rate_limiter.{" + key;
        String keys = prefix + "}";
        return Collections.singletonList(keys);
    }

}
