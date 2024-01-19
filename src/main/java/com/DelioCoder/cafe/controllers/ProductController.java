package com.DelioCoder.cafe.controllers;

import com.DelioCoder.cafe.DTO.ProductDTO;
import com.DelioCoder.cafe.POJO.Product;
import com.DelioCoder.cafe.constant.CoffeConstants;
import com.DelioCoder.cafe.interfaces.product.ProductRest;
import com.DelioCoder.cafe.services.ProductServiceImpl;
import com.DelioCoder.cafe.utils.CoffeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ProductController implements ProductRest {

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Override
    public ResponseEntity<List<ProductDTO>> retrieveAllProducts() {

        try {

            return productServiceImpl.retrieveAllProducts();

        }catch (Exception ex)
        {
            log.error("Error " + ex);
        }

        return new ResponseEntity<List<ProductDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<ProductDTO> retrieveOneProductById(Integer id) {

        try{

            return productServiceImpl.retrieveOneProduct(id);

        }catch (Exception ex)
        {
            log.error("Error: " + ex);
        }

        return new ResponseEntity<ProductDTO>(new ProductDTO(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<ProductDTO>> retrieveProductsByCategory(Integer categoryId) {

        try {

            return productServiceImpl.retrieveProductsByCategory(categoryId);

        }catch (Exception ex)
        {
            log.error("Error: " + ex);
        }

        return new ResponseEntity<List<ProductDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {

        try {

           return this.productServiceImpl.addNewProduct(requestMap);

        }catch (Exception ex){
            log.error("Error: ", ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {

        try {

            return this.productServiceImpl.updateProduct(requestMap);

        }catch (Exception ex)
        {
            log.error("Error: " + ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateStatus(Integer id, Map<String, String> requestMap) {

        try {

            return this.productServiceImpl.updateStatus(id, requestMap);

        }catch (Exception ex)
        {
            log.error("Error: " + ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        return null;
    }
}
