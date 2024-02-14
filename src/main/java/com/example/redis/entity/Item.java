package com.example.redis.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Setter
  private String name;
  @Setter
  private String description;
  @Setter
  private Integer price;
  @Setter
  private Integer stock;

}
