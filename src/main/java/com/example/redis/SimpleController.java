package com.example.redis;

import com.example.redis.dto.PersonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class SimpleController {
  // 문자열 Key와 문자열로 구성된 Value를 다루기 위한 RedisTemplate
  // (Java 기준 Value가 문자열이란 뜻이다.)
  private final StringRedisTemplate redisTemplate;

  // Put을 통해 Key-Value를 설정해서 Redis에 저장
  @PutMapping("string")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setString(
    @RequestParam("key")
    String key,
    @RequestParam("value")
    String value
  ) {
    // Redis에 String을 저장하고 싶다
    // ValueOperations: Redis 기준 문자열 작업을 위한 클래스
    ValueOperations<String, String> operations
      = redisTemplate.opsForValue();
    // SET key value
    operations.set(key, value);

//    // List를 위한 클래스
//    ListOperations<String, String> listOperations
//      = redisTemplate.opsForList();
//    listOperations.leftPush(key, value);
//    listOperations.leftPop(key);

//    // Set을 위한 클래스
//    SetOperations<String, String> setOperations
//            = redisTemplate.opsForSet();
//    setOperations.add(key, value);
  }

  // Get을 통해서 데이터 회수
  @GetMapping("string")
  public String getString(
    @RequestParam("key")
    String key
  ) {
    ValueOperations<String, String> operations
      = redisTemplate.opsForValue();
    // GET key
    String value = operations.get(key); // nullable이다.
    if (value == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return value;

        /* // Sets 데이터 회수
        SetOperations<String, String> operations
                = redisTemplate.opsForSet();
        return operations.members(key);
        */
  }

  // 커스텀한 Configuration한 Bean 주입
  private final RedisTemplate<String, PersonDto> personRedisTemplate;

  @PutMapping("/person")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setPerson(
    @RequestParam("name")
    String name,
    @RequestBody
    PersonDto dto
  ) {
    ValueOperations<String, PersonDto> operations
      = personRedisTemplate.opsForValue();
    operations.set(name, dto);
  }

  @GetMapping("/person")
  public PersonDto getPerson(
    @RequestParam("name")
    String name
  ) {
    ValueOperations<String, PersonDto> operations
      = personRedisTemplate.opsForValue();
    PersonDto value = operations.get(name);
    if (value == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    return value;
  }
}