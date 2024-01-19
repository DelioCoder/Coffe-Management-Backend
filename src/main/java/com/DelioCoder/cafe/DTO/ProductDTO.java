package com.DelioCoder.cafe.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO {

    private Integer id;

    private String name;

    private String description;

    private Double price;

    private String status;

    private Integer categoryId;

    private String categoryName;

    public ProductDTO(Integer id, String name, String description, Double price, String status, Integer categoryId,String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public ProductDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProductDTO(Integer id, String name, String description, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
