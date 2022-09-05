package com.example.demo.entity;


import com.example.demo.exception.OutOfStockException;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String itemName;

    private int price;

    private int stockNumber;

    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;


    public void removeStock(int count) {
        int restStock = this.stockNumber - count;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber-=count;
    }
}
