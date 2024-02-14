package com.example.redis;

import com.example.redis.dto.ItemDto;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
// SlowDataQuery를 사용하는 ItemService
public class ItemService {
  private final SlowDataQuery repository;

  @Resource(name = "cacheRedisTemplate")
  private ValueOperations<Long, ItemDto> cacheOps;

  public List<ItemDto> readAll() {
    return repository.findAll()
      .stream()
      .map(ItemDto::fromEntity)
      .toList();
  }

  public ItemDto readOne(Long id) {
    // Cache Aside를 RedisTemplate을 활용해 직접 구현해 보자.
    // 1. cacheOps에서 ItemDto를 찾아본다.
    // GET id
    ItemDto found = cacheOps.get(id);
    // 2. null일 경우 데이터베이스에서 조회한다.
    if (found == null) {
      // 2-1. 없으면 404
      found = repository.findById(id)
        .map(ItemDto::fromEntity)
        .orElseThrow(() ->
          new ResponseStatusException(HttpStatus.NOT_FOUND));
      // 2-2. 있으면 캐시에 저장
      // 3번째 인자로 만료 시간 설정 가능
      cacheOps.set(id, found, Duration.ofSeconds(10));
    }

    // 3. 최종적으로 데이터를 변환한다.
    return found;

/*    return repository.findById(id)
      .map(ItemDto::fromEntity)
      .orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND));*/
  }
}
