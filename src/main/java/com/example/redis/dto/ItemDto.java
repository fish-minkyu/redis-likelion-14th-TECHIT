package com.example.redis.dto;

import com.example.redis.entity.Item;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
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
