package com.example.redis.repo;

import com.example.redis.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

  @Query("SELECT i " +
    "FROM Item i LEFT JOIN i.orders " +
    "WHERE size(i.orders) > 0 " +
    "ORDER BY size(i.orders) DESC " +
    "LIMIT 10")
  List<Item> top10MostSold();
}
