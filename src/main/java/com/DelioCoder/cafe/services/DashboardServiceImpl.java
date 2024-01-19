package com.DelioCoder.cafe.services;

import com.DelioCoder.cafe.dao.BillDao;
import com.DelioCoder.cafe.dao.CategoryDao;
import com.DelioCoder.cafe.dao.ProductDao;
import com.DelioCoder.cafe.interfaces.dashboard.DashboardService;
import com.DelioCoder.cafe.utils.CoffeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService
{

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private BillDao billDao;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {

        Map<String, Object> map = new HashMap<>();

        try {

            map.put("category", categoryDao.count());
            map.put("product", productDao.count());
            map.put("bill", billDao.count());

            return new ResponseEntity<>(map, HttpStatus.OK);

        }catch (Exception ex)
        {
            log.error("DashboardServiceImpl - Error: ", ex);
        }

        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
