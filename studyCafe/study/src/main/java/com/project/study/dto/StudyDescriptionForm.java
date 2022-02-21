package com.project.study.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class StudyDescriptionForm {

    @NotBlank
    @Length(min = 2,max = 60)
    private String shortDescription;

    @NotBlank
    private String fullDescription;
}
