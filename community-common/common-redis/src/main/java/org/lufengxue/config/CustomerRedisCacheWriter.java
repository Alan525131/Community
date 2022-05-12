//package org.lufengxue.config;
//
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.dao.PessimisticLockingFailureException;
//import org.springframework.data.redis.cache.RedisCacheWriter;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
//import org.springframework.data.redis.core.types.Expiration;
//import org.springframework.expression.spel.standard.SpelExpressionParser;
//import org.springframework.lang.Nullable;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//
//import java.nio.charset.StandardCharsets;
//import java.time.Duration;
//import java.util.Collections;
//import java.util.Optional;
//import java.util.concurrent.TimeUnit;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//
///**
// * 在原基础上扩展了按 key 设置过期时间
// * 例如: @Cacheable(cacheNames = CacheName.SYSTEM, key = " #userName + 'time = ' + #expire")
// */
//@Slf4j
//@Component
//public class CustomerRedisCacheWriter implements RedisCacheWriter {
//
//    /**
//     * 过期时间分隔符
//     */
//    private static final String EXPIRE_TIME_REGEX = "\\s*time\\s*=\\s*";
//
//    /**
//     * spEl解析器
//     */
//    private static SpelExpressionParser SPEL = new SpelExpressionParser();
//
//    private final RedisConnectionFactory connectionFactory;
//    private final Duration sleepTime;
//
//    /**
//     * @param connectionFactory must not be {@literal null}.
//     */
//    public CustomerRedisCacheWriter(RedisConnectionFactory connectionFactory) {
//        this(connectionFactory, Duration.ZERO);
//    }
//
//    /**
//     * @param connectionFactory must not be {@literal null}.
//     * @param sleepTime         sleep time between lock request attempts. Must not be {@literal null}. Use {@link Duration#ZERO}
//     *                          to disable locking.
//     */
//    CustomerRedisCacheWriter(RedisConnectionFactory connectionFactory, Duration sleepTime) {
//        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
//        Assert.notNull(sleepTime, "SleepTime must not be null!");
//        this.connectionFactory = connectionFactory;
//        this.sleepTime = sleepTime;
//        log.info("[初始化] {} 初始化完成.", "CustomerRedisCacheWriter");
//    }
//
//    /**
//     * (non-Javadoc)
//     *
//     * @see RedisCacheWriter#put(String, byte[], byte[], Duration)
//     */
//    @Override
//    public void put(@Nullable String name, @Nullable byte[] key, @Nullable byte[] value, @Nullable Duration ttl) {
//        Assert.notNull(name, "Name must not be null!");
//        Assert.notNull(key, "Key must not be null!");
//        Assert.notNull(value, "Value must not be null!");
//
//        execute(name, connection -> {
//            KeyInfo keyInfo = convertKey(key);
//            if (keyInfo.hasExpire) {
//                connection.set(keyInfo.reallyKey, value, Expiration.from(keyInfo.getTime(), TimeUnit.MILLISECONDS), SetOption.upsert());
//            } else if (shouldExpireWithin(ttl)) {
//                connection.set(key, value, Expiration.from(ttl.toMillis(), TimeUnit.MILLISECONDS), SetOption.upsert());
//            } else {
//                connection.set(key, value);
//            }
//            return "OK";
//        });
//    }
//
//    /**
//     * (non-Javadoc)
//     *
//     * @see RedisCacheWriter#get(String, byte[])
//     */
//    @Override
//    public byte[] get(@Nullable String name, @Nullable byte[] key) {
//
//        Assert.notNull(name, "Name must not be null!");
//        Assert.notNull(key, "Key must not be null!");
//
//        KeyInfo keyInfo = convertKey(key);
//        return execute(name, connection -> connection.get(keyInfo.hasExpire ? keyInfo.reallyKey : key));
//    }
//
//
//    /**
//     * (non-Javadoc)
//     *
//     * @see RedisCacheWriter#putIfAbsent(String, byte[], byte[], Duration)
//     */
//    @Override
//    public byte[] putIfAbsent(@Nullable String name, @Nullable byte[] key, @Nullable byte[] value, @Nullable Duration ttl) {
//        Assert.notNull(name, "Name must not be null!");
//        Assert.notNull(key, "Key must not be null!");
//        Assert.notNull(value, "Value must not be null!");
//
//        return execute(name, connection -> {
//            if (isLockingCacheWriter()) {
//                doLock(name, connection);
//            }
//            try {
//                boolean put;
//                KeyInfo keyInfo = convertKey(key);
//                if (keyInfo.hasExpire) {
//                    put = connection.set(keyInfo.reallyKey, value, Expiration.from(keyInfo.time, TimeUnit.MILLISECONDS), SetOption.ifAbsent());
//                } else if (shouldExpireWithin(ttl)) {
//                    put = connection.set(key, value, Expiration.from(ttl), SetOption.ifAbsent());
//                } else {
//                    put = connection.setNX(key, value);
//                }
//                if (put) {
//                    return null;
//                }
//                return connection.get(keyInfo.hasExpire ? keyInfo.reallyKey : key);
//            } finally {
//                if (isLockingCacheWriter()) {
//                    doUnlock(name, connection);
//                }
//            }
//        });
//    }
//
//    /**
//     * (non-Javadoc)
//     *
//     * @see RedisCacheWriter#remove(String, byte[])
//     */
//    @Override
//    public void remove(@Nullable String name, @Nullable byte[] key) {
//
//        Assert.notNull(name, "Name must not be null!");
//        Assert.notNull(key, "Key must not be null!");
//
//        execute(name, connection -> connection.del(key));
//    }
//
//    /**
//     * (non-Javadoc)
//     *
//     * @see RedisCacheWriter#clean(String, byte[])
//     */
//    @Override
//    public void clean(@Nullable String name, @Nullable byte[] pattern) {
//        Assert.notNull(name, "Name must not be null!");
//        Assert.notNull(pattern, "Pattern must not be null!");
//
//        execute(name, connection -> {
//            boolean wasLocked = false;
//            try {
//                if (isLockingCacheWriter()) {
//                    doLock(name, connection);
//                    wasLocked = true;
//                }
//                byte[][] keys = Optional.ofNullable(connection.keys(pattern)).orElse(Collections.emptySet())
//                        .toArray(new byte[0][]);
//                if (keys.length > 0) {
//                    connection.del(keys);
//                }
//            } finally {
//                if (wasLocked && isLockingCacheWriter()) {
//                    doUnlock(name, connection);
//                }
//            }
//            return "OK";
//        });
//    }
//
//    /**
//     * Explicitly set a write lock on a cache.
//     *
//     * @param name the name of the cache to lock.
//     */
//    void lock(String name) {
//        execute(name, connection -> doLock(name, connection));
//    }
//
//    /**
//     * Explicitly remove a write lock from a cache.
//     *
//     * @param name the name of the cache to unlock.
//     */
//    void unlock(String name) {
//        executeLockFree(connection -> doUnlock(name, connection));
//    }
//
//    private Boolean doLock(String name, RedisConnection connection) {
//        return connection.setNX(createCacheLockKey(name), new byte[0]);
//    }
//
//    private Long doUnlock(String name, RedisConnection connection) {
//        return connection.del(createCacheLockKey(name));
//    }
//
//    boolean doCheckLock(String name, RedisConnection connection) {
//        return connection.exists(createCacheLockKey(name));
//    }
//
//    /**
//     * @return {@literal true} if {@link RedisCacheWriter} uses locks.
//     */
//    private boolean isLockingCacheWriter() {
//        return !sleepTime.isZero() && !sleepTime.isNegative();
//    }
//
//    private <T> T execute(String name, Function<RedisConnection, T> callback) {
//        RedisConnection connection = connectionFactory.getConnection();
//        try {
//            checkAndPotentiallyWaitUntilUnlocked(name, connection);
//            return callback.apply(connection);
//        } finally {
//            connection.close();
//        }
//    }
//
//    private void executeLockFree(Consumer<RedisConnection> callback) {
//        RedisConnection connection = connectionFactory.getConnection();
//        try {
//            callback.accept(connection);
//        } finally {
//            connection.close();
//        }
//    }
//
//    private void checkAndPotentiallyWaitUntilUnlocked(String name, RedisConnection connection) {
//        if (!isLockingCacheWriter()) {
//            return;
//        }
//        try {
//            while (doCheckLock(name, connection)) {
//                Thread.sleep(sleepTime.toMillis());
//            }
//        } catch (InterruptedException ex) {
//            // Re-interrupt current thread, to allow other participants to react.
//            Thread.currentThread().interrupt();
//            throw new PessimisticLockingFailureException(String.format("Interrupted while waiting to unlock cache %s", name), ex);
//        }
//    }
//
//    private static boolean shouldExpireWithin(@Nullable Duration ttl) {
//        return ttl != null && !ttl.isZero() && !ttl.isNegative();
//    }
//
//    private static byte[] createCacheLockKey(String name) {
//        return (name + "~lock").getBytes(StandardCharsets.UTF_8);
//    }
//
//    private KeyInfo convertKey(byte[] key) {
//        KeyInfo keyInfo = new KeyInfo();
//        String k = new String(key);
//        String[] split = k.split(EXPIRE_TIME_REGEX);
//        if (split.length > 1) {
//            if (split.length != 2) {
//                throw new RuntimeException("定义的 key 不正确: " + k);
//            }
//            String time = split[1];
//            Long longTime = SPEL.parseExpression(time).getValue(Long.class);
//            if (longTime != null && (longTime > 0 || longTime == -1)) {
//                keyInfo.hasExpire = true;
//                keyInfo.time = longTime;
//                keyInfo.reallyKey = split[0].getBytes();
//            } else {
//                throw new RuntimeException("定义的过期时间不正确: " + longTime);
//            }
//        }
//        return keyInfo;
//    }
//
//
//    @Data
//    private static class KeyInfo {
//        boolean hasExpire = false;
//        byte[] reallyKey;
//        long time;
//    }
//
//}
