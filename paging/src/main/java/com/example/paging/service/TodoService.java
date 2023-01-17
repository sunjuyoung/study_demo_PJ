package com.example.paging.service;

import com.example.paging.domain.Todo;
import com.example.paging.dto.PageRequetDTO;
import com.example.paging.dto.PageResponseDTO;
import com.example.paging.dto.TodoDTO;
import com.example.paging.repository.TodoMapperRepository;
import com.example.paging.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.management.modelmbean.ModelMBean;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoMapperRepository todoMapperRepository;
    private final ModelMapper modelMapper;
    private final TodoRepository todoRepository;
    public void register(TodoDTO todoDTO) {
        Todo map = modelMapper.map(todoDTO, Todo.class);
        todoMapperRepository.save1(map);

    }

    public TodoDTO getTodo(Long tno) {
        Todo todo = todoMapperRepository.getTodo(tno);
        return modelMapper.map(todo,TodoDTO.class);
    }

    public List<TodoDTO> getTodos() {
        List<Todo> todos = todoMapperRepository.getTodos();

        return null;
    }

    public PageResponseDTO<TodoDTO> list(PageRequetDTO pageRequetDTO) {

        Pageable pageable = pageRequetDTO.getPageable("tno");
        String keyword = pageRequetDTO.getKeyword();
        String type = pageRequetDTO.getType();
        Page<Todo> result = todoRepository.searchList(pageable, keyword, type);
        List<TodoDTO> dtos = result.getContent().stream()
                .map(todo ->
                    modelMapper.map(todo, TodoDTO.class)
                ).collect(Collectors.toList());


        return new PageResponseDTO<>(pageRequetDTO,dtos,(int)result.getTotalPages());
    }
}
