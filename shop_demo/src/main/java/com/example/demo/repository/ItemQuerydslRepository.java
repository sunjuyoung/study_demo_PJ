package com.example.demo.repository;

import com.example.demo.entity.Item;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ItemQuerydslRepository extends QuerydslPredicateExecutor<Item> {

}
