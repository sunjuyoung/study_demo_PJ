package com.example.ddd.order;

import com.example.ddd.category.Product;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table
public class OrderLine {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;
    
    private int price;
    private int quantity;
    private int amount;

    protected OrderLine(){

    }

    public OrderLine(Product product, int price, int quantity) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.amount = calculateAmount();
    }

    public int calculateAmount(){
        return price * quantity;
    }
}
