package com.DelioCoder.cafe.services;

import com.DelioCoder.cafe.DTO.ProductDTO;
import com.DelioCoder.cafe.POJO.Category;
import com.DelioCoder.cafe.POJO.Product;
import com.DelioCoder.cafe.config.JWT.JwtFilter;
import com.DelioCoder.cafe.constant.CoffeConstants;
import com.DelioCoder.cafe.dao.ProductDao;
import com.DelioCoder.cafe.interfaces.product.ProductService;
import com.DelioCoder.cafe.utils.CoffeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private ProductDao productDao;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {

        log.info("inside addNewProduct");

        try {

            if(this.jwtFilter.isAdmin())
            {
                if(validateProductMap(requestMap, false)){

                    this.productDao.save(getProductFromMap(requestMap, false));

                    return CoffeUtils.getResponseEntity("Product Added Successfully", HttpStatus.CREATED);

                }

                return CoffeUtils.getResponseEntity(CoffeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);

            }else

                return CoffeUtils.getResponseEntity(CoffeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);

        }catch (Exception ex)
        {
            log.error("Error: ", ex);


        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<ProductDTO>> retrieveAllProducts() {

        try {

            return new ResponseEntity<List<ProductDTO>>(productDao.getAllProduct(), HttpStatus.OK);

        }catch (Exception ex)
        {
            log.error("Error: " + ex);
        }

        return new ResponseEntity<List<ProductDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<ProductDTO> retrieveOneProduct(Integer id) {

        try {

            return new ResponseEntity<ProductDTO>(productDao.getOneProductById(id), HttpStatus.OK);


        }catch (Exception ex)
        {
            log.error("Error: " + ex);
        }

        return new ResponseEntity<ProductDTO>( new ProductDTO(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {

            if(this.jwtFilter.isAdmin())
            {

                if(validateProductMap(requestMap, true))
                {
                    Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));

                    if(optional.isPresent())
                    {
                        Product product = getProductFromMap(requestMap, true);

                        product.setStatus(optional.get().getStatus());

                        productDao.save(product);

                        return CoffeUtils.getResponseEntity("Product Updated Successfully", HttpStatus.OK);
                    }else {
                        return CoffeUtils.getResponseEntity("Product id does not exist", HttpStatus.OK);
                    }
                }

            }else {
                return CoffeUtils.getResponseEntity(CoffeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            log.error("Error: " + ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {

            if(this.jwtFilter.isAdmin())
            {

                productDao.deleteById(id);

                return CoffeUtils.getResponseEntity("Product Deleted Successfully", HttpStatus.OK);

            }else {
                return CoffeUtils.getResponseEntity(CoffeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            log.error("Error: " + ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Integer id, Map<String, String> requestMap) {

        log.info("Inside updateStatus " + id + requestMap.get("status"));

        try {

            if(this.jwtFilter.isAdmin())
            {

                Optional optional = productDao.findById(id);

                if(optional.isPresent())
                {

                    productDao.updateProductStatus(requestMap.get("status"), id);

                    return CoffeUtils.getResponseEntity("Product Status Updated Successfully", HttpStatus.OK);

                }else {
                    return CoffeUtils.getResponseEntity("Product id does not exist", HttpStatus.OK);
                }

            }else {
                return CoffeUtils.getResponseEntity(CoffeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex)
        {
            log.error("Error: " + ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductDTO>> retrieveProductsByCategory(Integer id) {

        try {

            return new ResponseEntity<List<ProductDTO>>(productDao.getProductsByCategory(id), HttpStatus.OK);

        }catch (Exception ex)
        {
            log.error("Error: " + ex);
        }

        return new ResponseEntity<List<ProductDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {

        Category category = new Category();

        category.setId(Integer.parseInt(requestMap.get("categoryId")));


        Product product = new Product();

        if(isAdd)
        {
            product.setId(Integer.parseInt(requestMap.get("id")));

        }else {
            product.setStatus("true");
        }

        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Double.parseDouble(requestMap.get("price")));


        return product;

    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId)
    {
        if (requestMap.containsKey("name"))
        {
            if (requestMap.containsKey("id") && validateId)
            {
                return true;

            }else return !validateId;
        }
        return false;
    }

}
