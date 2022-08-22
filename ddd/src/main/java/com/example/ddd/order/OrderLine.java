package com.example.ddd.order;

import com.example.ddd.category.Product;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "order_line")
public class OrderLine {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
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
