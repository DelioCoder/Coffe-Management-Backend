package com.DelioCoder.cafe.interfaces.category;

import com.DelioCoder.cafe.POJO.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryRest {

    @GetMapping(path = "/getAll")
    ResponseEntity<List<Category>> retrieveAllCategories(@RequestParam(required = false) String filterValue);

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required = true)Map<String, String> requestMap);

    @PutMapping(path = "/update")
    ResponseEntity<String> updateCategory(@RequestBody(required = true)Map<String, String> requestMap);

    @DeleteMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteCategory(@PathVariable int id);

}
