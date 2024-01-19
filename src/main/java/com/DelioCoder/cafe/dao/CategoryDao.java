package com.DelioCoder.cafe.dao;

import com.DelioCoder.cafe.POJO.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {

    public List<Category> getALlCategory();



}