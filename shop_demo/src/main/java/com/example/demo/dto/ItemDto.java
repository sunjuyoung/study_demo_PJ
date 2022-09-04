package com.example.demo.dto;

import com.example.demo.entity.ItemSellStatus;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
public class ItemDto {

    private String itemName;

    private int price;

    private int stockNumber;

    private String itemDetail;

    private String itemSellStatus;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
