package com.springboot.test.data.repository;

import com.springboot.test.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //findByNumber 메서드와 동일하게 동작 ,equals 와 동일한 기능
    Product findByNumberIs(Long number);


    List<Product> findByUpdatedAtNull();
}
