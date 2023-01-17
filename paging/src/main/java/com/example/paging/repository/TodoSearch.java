package com.example.paging.repository;

import com.example.paging.domain.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoSearch {

    Page<Todo> searchList(Pageable pageable);

    Page<Todo> searchList(Pageable pageable,String keyword, String type);

}
