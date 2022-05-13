package org.lufengxue.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;


/**
 * 分布式锁
 */
@Slf4j
@Component
public class RedisLock {

    public RedisLock() {
        log.info("[初始化] {} 初始化完成.", "RedisLock");
    }

    private static final String LOCK_PREF = "lock:";
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 该加锁方法基于 Redis 可实现分布式加锁
     *
     * @param lockName 锁名称
     * @param lockKey  锁钥匙
     * @param seconds  锁过期时间(秒), 建议设置时间长一点, 避免在调用 unlock 方法前失效.
     */
    public TheLock lock(String lockName, String lockKey, long seconds) {
        if (StringUtils.isAnyBlank(lockName, lockKey) || seconds < 1) {
            throw new RuntimeException("加锁信息异常");
        }

        String lockStr = LOCK_PREF.concat(lockName);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        log.info("正在抢锁, 锁名 {}", lockName);
        long t1 = System.currentTimeMillis();
        Boolean lock;
        for (; ; ) {
            lock = ops.setIfAbsent(lockStr, lockKey, seconds, TimeUnit.SECONDS);
            if (lock != null && lock) {
                long t2 = System.currentTimeMillis();
                log.info("已抢到锁, 锁名 {}, 钥匙 {}, 过期时间 {} 秒, 抢锁耗时 {} 毫秒.", lockName, lockKey, seconds, (t2 - t1));
                return new TheLock(t2, lockName, lockKey);
            }
            if (System.currentTimeMillis() - t1 > 10000) {
                throw new RuntimeException(String.format("10 秒抢锁失败: 锁名 %s", lockName));
            }
            Thread.yield();
        }
    }

    /**
     * Redis 解锁
     *
     * @param theLock 锁对象
     */
    public void unlock(TheLock theLock) {
        if (theLock == null) {
            throw new RuntimeException("解锁信息异常");
        }

        String lockStr = LOCK_PREF.concat(theLock.lockName);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String lockValue = ops.get(lockStr);
        if (StringUtils.isBlank(lockValue)) {
            log.error("解锁失败: 锁已不存在, 锁名 {}", theLock.lockName);
            return;
        }
        if (!theLock.lockKey.equals(lockValue)) {
            log.error("解锁失败: 钥匙不匹配, 锁名 {}, 开锁钥匙 {}, 正确锁钥匙 {}", theLock.lockName, theLock.lockKey, lockValue);
            return;
        }
        Long execute = redisTemplate.execute(new DefaultRedisScript<Long>(RELEASE_LOCK_SCRIPT, Long.class), Collections.singletonList(lockStr), theLock.lockKey);
        if (execute != null && execute.equals(1L)) {
            long now = System.currentTimeMillis();
            log.info("解锁成功: 锁名 {}, 锁时长 {} 毫秒", theLock.lockName, now - theLock.lockTime);
        } else {
            log.error("解锁失败: 锁名 {}, 锁钥匙 {}, 解锁结果 {}", theLock.lockName, theLock.lockKey, execute);
        }
    }

    public static class TheLock {
        private final long lockTime;
        private final String lockName;
        private final String lockKey;

        TheLock(long lockTime, String lockName, String lockKey) {
            this.lockTime = lockTime;
            this.lockName = lockName;
            this.lockKey = lockKey;
        }
    }

}
