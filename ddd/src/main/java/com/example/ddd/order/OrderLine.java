package com.example.ddd.order;

import com.example.ddd.category.Product;
import lombok.Data;

@Data
public class OrderLine {

    private Product product;
    private int price;
    private int quantity;
    private int amount;


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
