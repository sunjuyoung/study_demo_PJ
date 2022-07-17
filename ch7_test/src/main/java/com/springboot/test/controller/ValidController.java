package com.springboot.test.controller;

import com.springboot.test.data.dto.ValidRequestDto;
import com.springboot.test.group.ValidationGroup1;
import com.springboot.test.group.ValidationGroup2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/validation")
public class ValidController {

    private final Logger LOGGER = LoggerFactory.getLogger(ValidController.class);

    @PostMapping("/validated")
    public ResponseEntity<String> checkvalid(@Validated @RequestBody ValidRequestDto validRequestDto){
        LOGGER.info(validRequestDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(validRequestDto.toString());

    }
    @PostMapping("/validated/group1")
    public ResponseEntity<String> checkvalid1(@Validated(ValidationGroup1.class) @RequestBody ValidRequestDto validRequestDto){
        LOGGER.info(validRequestDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(validRequestDto.toString());

    }
    @PostMapping("/validated/group2")
    public ResponseEntity<String> checkvalid2(@Validated(ValidationGroup2.class) @RequestBody ValidRequestDto validRequestDto){
        LOGGER.info(validRequestDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(validRequestDto.toString());

    }
    
    //validated 어노테이션에 특정 그룹을 지정하지 않는 경우에는 groups 속성을 설정하지 않는 필드에 대해서만 유효성 검사를 실시한다
    @PostMapping("/validated/allGroup")
    public ResponseEntity<String> checkvalid3(@Validated({ValidationGroup2.class,ValidationGroup1.class})
                                                  @RequestBody ValidRequestDto validRequestDto){
        LOGGER.info(validRequestDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(validRequestDto.toString());

    }
}

