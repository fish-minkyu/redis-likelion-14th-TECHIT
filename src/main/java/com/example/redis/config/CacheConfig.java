package com.example.redis.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
// @EnableCaching
// : 캐시를 어노테이션을 바탕으로 만들 수 있게 해주는 기능
// (캐시를 어떻게 다룰지 결정하는 cacheManager 필요)
@EnableCaching
public class CacheConfig {
  @Bean
  public RedisCacheManager cacheManager(
    // RedisConnectionFactory
    // : 어떤 식으로 Redis와 연결할지 구성되어 있다.
    RedisConnectionFactory redisConnectionFactory
  ) {
    // RedisCacheConfiguration
    // : 캐시를 어떤 식으로 구성하고 싶은지에 대한 설정
    RedisCacheConfiguration configuration = RedisCacheConfiguration
      .defaultCacheConfig()
      // null을 캐싱할 것인가 말 것인가?
      .disableCachingNullValues()
      // Time To Live(Ttl): 만료 시간
      .entryTtl(Duration.ofSeconds(60))
      // Key 접두사 설정 (객체를 구분하기 위해 필요)
      .computePrefixWith(CacheKeyPrefix.simple())
      // Value 직렬화 / 역직렬화 방법
      .serializeValuesWith(
        SerializationPair.fromSerializer(RedisSerializer.json())
      );

    // Cacheable의 cacheName을 바탕으로 적용되는 규칙을 바꿔보자
    Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
    RedisCacheConfiguration itemAllConfig = RedisCacheConfiguration
      .defaultCacheConfig()
      .disableCachingNullValues()
      .entryTtl(Duration.ofSeconds(10))
      .serializeValuesWith(
        SerializationPair.fromSerializer(RedisSerializer.java())
      );
    // 이름이 "itemAllCache"이면 itemAllConfig 설정 적용
    configMap.put("itemAllCache", itemAllConfig);

    // 실제 매니저를 등록하는 과정
    return RedisCacheManager
      // Connection 전달
      .builder(redisConnectionFactory)
      // 위에서 만든 설정을 기본값으로 설정
      .cacheDefaults(configuration)
      // 캐시 이름에 따라 설정을 따로 적용할 수 있다.
      // Map으로 put하는 방법 외, cacheName과 설정 객체를 넣어서 적용시킬 수 있다.
//      .withCacheConfiguration("itemAllCache", itemAllConfig)
      .withInitialCacheConfigurations(configMap)
      .build();
  }
}
