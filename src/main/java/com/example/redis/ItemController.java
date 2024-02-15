package com.example.redis;

import com.example.redis.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
// SlowDataQuery를 기반으로 한 repo가 충분히 시간이 걸리는지 확인
public class ItemController {
  private final ItemService itemService;

  @PostMapping
  public ItemDto create(
    @RequestBody
    ItemDto itemDto
  ) {
    return itemService.create(itemDto);
  }

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

  // 구매 Handler Method
  @PostMapping("/{id}/purchase")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void purchase(
    @PathVariable("id") Long id
  ) {
    itemService.purchase(id);
  }
}
