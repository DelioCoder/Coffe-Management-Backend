package com.DelioCoder.cafe.controllers;

import com.DelioCoder.cafe.POJO.Category;
import com.DelioCoder.cafe.constant.CoffeConstants;
import com.DelioCoder.cafe.interfaces.category.CategoryRest;
import com.DelioCoder.cafe.interfaces.category.CategoryService;
import com.DelioCoder.cafe.services.CategoryServiceImp;
import com.DelioCoder.cafe.utils.CoffeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CategoryController implements CategoryRest {

    @Autowired
    CategoryServiceImp categoryService;

    @Override
    public ResponseEntity<List<Category>> retrieveAllCategories(String filterValue) {
        try {

            return categoryService.retrieveAllCategories(filterValue);

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try{

            return categoryService.addNewCategory(requestMap);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {

        try {

            return categoryService.updateCategory(requestMap);

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteCategory(int id) {
        try {

            return categoryService.deleteCategory(id);

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
