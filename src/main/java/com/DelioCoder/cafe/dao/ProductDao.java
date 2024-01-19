package com.DelioCoder.cafe.dao;

import com.DelioCoder.cafe.DTO.ProductDTO;
import com.DelioCoder.cafe.POJO.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {

    public List<ProductDTO> getAllProduct();

    @Modifying
    @Transactional
    public void updateProductStatus(@Param("status") String status, @Param("id") Integer id);

    public List<ProductDTO> getProductsByCategory(@Param("id") Integer categoryId);

    public ProductDTO getOneProductById(@Param("id") Integer id);

}
