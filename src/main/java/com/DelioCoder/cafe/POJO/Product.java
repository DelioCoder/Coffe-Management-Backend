package com.DelioCoder.cafe.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "Product.getAllProduct", query = "SELECT new com.DelioCoder.cafe.DTO.ProductDTO(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) from Product p")
@NamedQuery(name = "Product.updateProductStatus", query = "UPDATE Product p SET p.status =:status WHERE p.id =:id")
@NamedQuery(name = "Product.getProductsByCategory", query = "SELECT new com.DelioCoder.cafe.DTO.ProductDTO(p.id, p.name) from Product p WHERE p.category.id=:id and p.status='true'")
@NamedQuery(name = "Product.getOneProductById", query = "SELECT new com.DelioCoder.cafe.DTO.ProductDTO(p.id, p.name, p.description, p.price) FROM Product p WHERE p.id=:id")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "status")
    private String status;

}
