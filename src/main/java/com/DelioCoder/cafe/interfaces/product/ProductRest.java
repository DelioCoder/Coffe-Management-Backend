package com.DelioCoder.cafe.interfaces.product;

import com.DelioCoder.cafe.DTO.ProductDTO;
import com.DelioCoder.cafe.POJO.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/product")
public interface ProductRest {

    @GetMapping(path = "/get")
    public ResponseEntity<List<ProductDTO>> retrieveAllProducts();

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<ProductDTO> retrieveOneProductById(@PathVariable Integer id);

    @GetMapping(path = "/getByCategory/{categoryId}")
    public ResponseEntity<List<ProductDTO>> retrieveProductsByCategory(@PathVariable Integer categoryId);


    @PostMapping(path = "/add")
    public ResponseEntity<String> addNewProduct(@RequestBody Map<String, String> requestMap);

    @PutMapping(path = "/update")
    public ResponseEntity<String> updateProduct(@RequestBody Map<String,String> requestMap);

    @PutMapping(path = "/update-status/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable(required = true) Integer id, @RequestBody Map<String, String> requestMap);

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(required = true) Integer id);

}
