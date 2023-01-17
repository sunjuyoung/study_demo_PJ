package com.example.paging.repository;

import com.example.paging.domain.QTodo;
import com.example.paging.domain.Todo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.example.paging.domain.QTodo.*;

public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch{


    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> searchList(Pageable pageable) {

        JPQLQuery<Todo> query = from(todo);


        this.getQuerydsl().applyPagination(pageable,query);
        List<Todo> fetch = query.fetch();
        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Todo> searchList(Pageable pageable, String keyword, String type) {


        JPQLQuery<Todo> query = from(todo);

        if(keyword != null && type != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            switch (type){
                case "t":
                    booleanBuilder.or(todo.title.contains(keyword));
                    break;
                case "c":
                    booleanBuilder.or(todo.content.contains(keyword));
                    break;
                case "w":
                    booleanBuilder.or(todo.writer.contains(keyword));
                    break;
            }
            query.where(booleanBuilder);
        }
        this.getQuerydsl().applyPagination(pageable,query);
        long count = query.fetchCount();
        List<Todo> fetch = query.fetch();


        return new PageImpl<>(fetch,pageable,count);
    }
}
