package com.example.paging.repository;

import com.example.paging.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo,Long>, TodoSearch {
}
