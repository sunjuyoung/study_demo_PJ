package com.example.ddd.category;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Table(name = "product")
@NoArgsConstructor
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_category",joinColumns = @JoinColumn(name = "product_id"))
    private List<CategoryId> categoryIds;

    private String detail;

    private int price;

}
