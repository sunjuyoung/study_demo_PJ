package com.example.paging.controller;


import com.example.paging.dto.PageRequetDTO;
import com.example.paging.dto.PageResponseDTO;
import com.example.paging.dto.TodoDTO;
import com.example.paging.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;


    @PostMapping("/todo/register")
    public ResponseEntity<?> register(@RequestBody TodoDTO todoDTO){
        todoService.register(todoDTO);
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<?> register(@PathVariable("id")Long id){
        TodoDTO todoDTO = todoService.getTodo(id);
        return ResponseEntity.ok().body(todoDTO);
    }
    @GetMapping("/todo")
    public PageResponseDTO<TodoDTO> register(PageRequetDTO pageRequetDTO){
        //List<TodoDTO> todoDTOS = todoService.getTodos();

        PageResponseDTO<TodoDTO> todoDTOS = todoService.list(pageRequetDTO);
        return todoDTOS;
    }
}
