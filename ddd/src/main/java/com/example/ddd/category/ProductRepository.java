package com.example.ddd.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select p from Product p where :catId member of ")
    Product findProductByCategoryIds(@Param("catId") CategoryId catId, @Param("page")int page, @Param("size")int size);
}
