package org.lufengxue.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.lufengxue.contanents.CacheName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Slf4j
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * Redis 模板
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 多个序列化供君选择
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        RedisSerializer<Object> fastJsonSerializer = new GenericFastJsonRedisSerializer();
        RedisSerializer<Object> fastJsonSerializer2 = new FastJsonRedisSerializer<>(Object.class);
        RedisSerializer<Object> jacksonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // 设置key的序列化规则
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);

        // 设置Value的序列化规则
        redisTemplate.setValueSerializer(fastJsonSerializer);
        redisTemplate.setHashValueSerializer(fastJsonSerializer);

        // 设置支持事物
        //redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        log.info("[初始化] {} 初始化完成.", "redisTemplate");
        return redisTemplate;
    }


    /**
     * 开启 spring 缓存注解
     * <p>
     * 注解 @Cacheable
     * 根据键从缓存中取值，存在获取到后直接返回。
     * 键不存在则执行方法，将返回结果放入到缓存中再返回。
     * <p>
     * 注解 @CachePut
     * 先执行方法，将返回结果放入到缓存中, 最后返回结果
     * <p>
     * 注解 @CacheEvict:
     * 执行方法后，删除缓存中数据
     */
//    @Bean
//    @Override
//    public RedisCacheManager cacheManager() {
//        Map<String, RedisCacheConfiguration> configMap = new HashMap<>(2);
//        // 系统数据, 默认缓存 8 小时
////        configMap.put(CacheName.SYSTEM, creatConfig(1000 * 60 * 60 * 8));
//        // 用户数据, 默认缓存 2 小时
//        configMap.put(CacheName.BUILDING_FLOOR_NUMBER, creatConfig(1000 * 60 * 60 * 2));
//        RedisCacheManager redisCacheManager = RedisCacheManager
//                .builder(new CustomerRedisCacheWriter(redisConnectionFactory))
//                .withInitialCacheConfigurations(configMap)
//                .cacheDefaults(configMap.get(CacheName.BUILDING_FLOOR_NUMBER))
//                .build();
//
//        log.info("[初始化] {} 初始化完成.", "RedisCacheManager");
//        return redisCacheManager;
//    }


    /**
     * 自定义 Key 生成器, 在注解没有指定 Key 时, 将使用此生成器生成的 Key.
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        log.info("[初始化] {} 初始化完成.", "KeyGenerator");
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getSimpleName())
                    .append(":")
                    .append(method.getName());
            if (params.length > 0) {
                sb.append("#");
            }
            for (Object obj : params) {
                sb.append(obj);
                sb.append(".");
            }
            if (params.length > 0) {
                return sb.substring(0, sb.length() - 1);
            }
            return sb.toString();
        };
    }


    /**
     * Redis消息监听器
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        log.info("[初始化] {} 初始化完成.", "RedisMessageListenerContainer");
        return container;
    }


    /**
     * 创建配置
     */
    @SuppressWarnings("all")
    private RedisCacheConfiguration creatConfig(int millis) {
        RedisTemplate<String, Object> template = redisTemplate();
        RedisSerializer keySerializer = template.getKeySerializer();
        RedisSerializer<?> valueSerializer = template.getValueSerializer();

        return RedisCacheConfiguration.defaultCacheConfig()
                // 设置统一缓存过期时间
                .entryTtl(Duration.ofMillis(millis))
                // 设置前缀拼接规则
                .computePrefixWith(cacheName -> cacheName + ":")
                // 设置 Key 序列化器
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                // 设置 value 序列化器
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer));
    }

}
