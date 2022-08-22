package com.example.ddd.category;


import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
public class Category {

    @EmbeddedId
    private CategoryId id;

    private String name;

    protected Category() {
    }

    public Category(CategoryId id, String name) {
        this.id = id;
        this.name = name;
    }
}
