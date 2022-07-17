package com.springboot.test.data.dto;

import com.springboot.test.group.ValidationGroup1;
import com.springboot.test.group.ValidationGroup2;
import jdk.jfr.DataAmount;
import lombok.*;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ValidRequestDto {

    @NotBlank
    String name;

    @Email
    String email;

    @Pattern(regexp = "010[.-]?(\\d{4})[.-]?(\\d{4})$")
    String phoneNumber;

    @Min(value = 20, groups = ValidationGroup1.class) @Max(value = 40, groups = ValidationGroup1.class)
    int age;

    @Size(min = 0,max = 40)
    String desc;

    @Positive(groups = ValidationGroup2.class) //양수 허용  PositiveOrZero 0포함
    int count;

    @AssertTrue //true 인지 체크 null값은 체크하지 않는다
    boolean booleanCheck;
}
