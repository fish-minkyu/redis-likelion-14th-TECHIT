package com.example.redis.dto;

import com.example.redis.entity.Item;
import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
// implements Serializable
// : 객체를 직렬화하기 위한 마커(marker) 인터페이스
public class ItemDto implements Serializable {
  private Long id;
  private String name;
  private String description;
  private Integer price;
  private Integer stock;

  public static ItemDto fromEntity(Item entity) {
    return ItemDto.builder()
      .id(entity.getId())
      .name(entity.getName())
      .description(entity.getDescription())
      .price(entity.getPrice())
      .stock(entity.getStock())
      .build();
  }
}
