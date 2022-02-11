package com.project.study.dto;

import com.project.study.domain.Zone;
import lombok.Data;

import javax.persistence.Column;

@Data
public class ZoneForm {


    private String zoneName;


    public String getCityName(){
        return zoneName.substring(0,zoneName.indexOf("("));
    }
    public String getLocalNameOfCity(){
        return zoneName.substring(zoneName.indexOf("(") +1 ,zoneName.indexOf(")"));
    }
    public String getProvinceName(){
        return zoneName.substring(zoneName.indexOf("/")+1);
    }

    public Zone getZone(){
        return Zone.builder().city(getCityName())
                .localNameOfCity(getLocalNameOfCity())
                .province(getProvinceName())
                .build();
    }
}
