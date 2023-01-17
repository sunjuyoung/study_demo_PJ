package com.example.paging.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoDTO {

    private Long tno;

    @NotNull
    private String title;

    private String content;

    private LocalDate dueDate;

    private boolean finished;

    @NotNull
    private String writer;
}
