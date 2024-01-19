package com.DelioCoder.cafe.interfaces.product;

import com.DelioCoder.cafe.DTO.ProductDTO;
import com.DelioCoder.cafe.POJO.Product;
import com.DelioCoder.cafe.services.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {

    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap);

    public ResponseEntity<List<ProductDTO>> retrieveAllProducts();

    public ResponseEntity<ProductDTO> retrieveOneProduct(Integer id);

    public ResponseEntity<String> updateProduct(Map<String, String> requestMap);

    public ResponseEntity<String> deleteProduct(Integer id);

    public ResponseEntity<String> updateStatus(Integer id, Map<String, String> requestMap);

    public ResponseEntity<List<ProductDTO>> retrieveProductsByCategory(Integer id);

}
