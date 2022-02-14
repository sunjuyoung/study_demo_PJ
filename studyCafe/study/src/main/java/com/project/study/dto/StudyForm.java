package com.project.study.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class StudyForm {
    @NotBlank
    @Length(min = 2,max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9]{2,20}$",message = "2~20자의 영문 소문자,숫자를 입력해주세요, 특수 기호는 불가능합니다.")
    private String path;

    @NotBlank
    @Length(min = 2,max = 30)
    private String title;

    @NotBlank
    @Length(min = 2,max = 60)
    private String shortDescription;


    @NotBlank
    private String fullDescription;
}
