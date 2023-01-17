package com.example.paging.repository;

import com.example.paging.domain.Todo;
import com.example.paging.dto.TodoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TodoMapperRepository {

    void save1(Todo Todo);

    List<Todo> getTodos();

    Todo getTodo(Long tno);


}
