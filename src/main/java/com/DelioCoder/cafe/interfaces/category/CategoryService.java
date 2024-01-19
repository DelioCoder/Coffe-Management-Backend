package com.DelioCoder.cafe.interfaces.category;

import com.DelioCoder.cafe.POJO.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap);

    public ResponseEntity<List<Category>> retrieveAllCategories(String filterValue);

    public ResponseEntity<String> updateCategory(Map<String, String> requestMap);

    public ResponseEntity<String> deleteCategory(Integer id);

}
