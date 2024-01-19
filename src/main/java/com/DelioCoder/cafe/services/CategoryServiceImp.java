package com.DelioCoder.cafe.services;

import com.DelioCoder.cafe.POJO.Category;
import com.DelioCoder.cafe.config.JWT.JwtFilter;
import com.DelioCoder.cafe.constant.CoffeConstants;
import com.DelioCoder.cafe.dao.CategoryDao;
import com.DelioCoder.cafe.interfaces.category.CategoryService;
import com.DelioCoder.cafe.utils.CoffeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
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
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        log.info("inside addNewCategory");

        try{

            if(this.jwtFilter.isAdmin())
            {

                if(validateCategoryMap(requestMap, false)){

                    this.categoryDao.save(getCategoryFromMap(requestMap, false));

                    return CoffeUtils.getResponseEntity("Category added Successfully", HttpStatus.CREATED);
                }

            }else {
                log.warn("Usuario no autorizado para agregar una nueva categoría. Rol actual: {}", jwtFilter.isAdmin() ? "admin" : "user");

                return CoffeUtils.getResponseEntity(CoffeConstants.UNAUTHORIZED_ACCESS, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex)
        {
            log.error("Error: " + ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> retrieveAllCategories(String filterValue) {
        try{

            if(!Strings.isEmpty(filterValue) && filterValue.equalsIgnoreCase("true"))
            {
                return new ResponseEntity<List<Category>>(categoryDao.getALlCategory(), HttpStatus.OK);
            }else{
                return new ResponseEntity<List<Category>>(categoryDao.findAll(), HttpStatus.OK);
            }

        }catch (Exception ex) {
            log.error("Error: " + ex);
        }

        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {

        try {

            if(this.jwtFilter.isAdmin())
            {

                if(validateCategoryMap(requestMap, true))
                {
                    Optional<Category> optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));

                    if(optional.isPresent())
                    {
                        categoryDao.save(getCategoryFromMap(requestMap, true));

                        return CoffeUtils.getResponseEntity("Category Updated Successfully", HttpStatus.OK);
                    }else {
                        return CoffeUtils.getResponseEntity("Category id does not exist", HttpStatus.OK);
                    }

                }

            }else {
                return CoffeUtils.getResponseEntity(CoffeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex) {
            log.error("Error: " + ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> deleteCategory(Integer id) {

        try {

            if(this.jwtFilter.isAdmin())
            {

                Optional<Category> optional = categoryDao.findById(id);

                if(optional.isPresent())
                {

                    categoryDao.deleteById(id);

                    return CoffeUtils.getResponseEntity("Category Deleted Successfully", HttpStatus.OK);

                }else {
                    return CoffeUtils.getResponseEntity("Category id does not exist", HttpStatus.OK);
                }

            }else {
                return CoffeUtils.getResponseEntity(CoffeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex)
        {
            log.error("Error: ", ex);

        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {

        if(requestMap.containsKey("name"))
        {
            if(requestMap.containsKey("id") && validateId)
            {
                return true;

            }else return !validateId;
        }

        return false;

    }

    private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd)
    {
        Category category = new Category();

        if(isAdd)
        {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }

        category.setName(requestMap.get("name"));
        return category;
    }

}
