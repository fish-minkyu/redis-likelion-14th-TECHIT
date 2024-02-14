package com.example.redis;

import com.example.redis.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
// SlowDataQuery를 기반으로 한 repo가 충분히 시간이 걸리는지 확인
public class ItemController {
  private final ItemService itemService;

  @GetMapping
  public List<ItemDto> readAll() {
    return itemService.readAll();
  }

  @GetMapping("/{id}")
  public ItemDto readOne(
    @PathVariable("id") Long id
  ) {
    return itemService.readOne(id);
  }
}
