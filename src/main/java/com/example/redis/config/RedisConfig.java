package com.example.redis.config;

import com.example.redis.dto.ItemDto;
import com.example.redis.dto.PersonDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
// @EnableRedisHttpSession
// : 2. 서버 인스턴스 세션 공유 설정
// maxInactiveIntervalInSeconds
// : 세션을 유지할 시간 설정
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 10)
public class RedisConfig {

  // PersonDto
  @Bean
  public RedisTemplate<String, PersonDto> personRedisTemplate(
    // RedisConnectionFactory: Redis와 연결해주는 객체
    RedisConnectionFactory connectionFactory
  ) {
    RedisTemplate<String, PersonDto> template = new RedisTemplate<>();
    // 연결을 어떻게 받아올 것인지 설정
    template.setConnectionFactory(connectionFactory);
    // 주어진 데이터의 직렬화 방식을 결정한다.
    // Redis의 Value은 결국 문자열의 형식이니까,
    // 주어진 데이터(DTO)를 어떻게 문자열로 바꿀 것인지를 정의
    // 미리 만들어진 JSON 변환기를 설정 (Jackson 라이브러리에게 객체를 직렬화하는 방식을 전달)
//    template.setDefaultSerializer(RedisSerializer.json());
    // 미리 만들어진 String 변환기를 설정
    template.setKeySerializer(RedisSerializer.string());
    // 미리 만들어진 JSON 변환기를 설정
    template.setValueSerializer(RedisSerializer.json());

    return template;
  }

  // ItemDto
  @Bean
  public RedisTemplate<Long, ItemDto> cacheRedisTemplate(
    RedisConnectionFactory connectionFactory
  ) {
    RedisTemplate<Long, ItemDto> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setDefaultSerializer(RedisSerializer.json());
    return template;
  }
}
