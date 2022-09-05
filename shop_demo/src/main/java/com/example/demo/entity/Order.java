package com.example.demo.entity;

import com.example.demo.entity.constant.OrderStatus;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@ToString
public class Order extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItemList = new ArrayList<>();


    public Order(OrderItem orderItem,Member member){
        this.member = member;
        this.orderItemList.add(orderItem);
        this.orderDate = LocalDateTime.now();
        this.orderStatus = OrderStatus.ORDER;

    }

    public void addOrderItem(OrderItem orderItem){
        orderItemList.add(orderItem);
        orderItem.setOrder(this);
    }

    public int orderTotalPrice(){
        int total = 0;
        for(OrderItem orderItem : orderItemList){
            total+=orderItem.getTotalPrice();
        }
        return total;
    }




}
