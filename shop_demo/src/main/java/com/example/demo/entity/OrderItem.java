package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문가격

    private int count; //수량

    private int totalPrice;

    protected OrderItem(){

    }

    public OrderItem(Item item,int count){
        this.item = item;
        this.count = count;
        this.orderPrice = item.getPrice();
        getTotalPrice();
        item.removeStock(count);
    }

    public void totalPrice() {
        this.totalPrice = count * orderPrice;
    }



}
