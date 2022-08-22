package com.example.ddd.category;

import lombok.Getter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Access(AccessType.FIELD)
public class CategoryId implements Serializable {

    @Column(name = "category_id")
    private Long value;

    protected CategoryId() {
    }
    public CategoryId(Long value) {
        this.value = value;
    }
}
