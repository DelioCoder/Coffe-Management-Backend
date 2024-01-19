package com.DelioCoder.cafe.controllers;

import com.DelioCoder.cafe.interfaces.dashboard.DashboardRest;
import com.DelioCoder.cafe.interfaces.dashboard.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class DashboardController implements DashboardRest {

    @Autowired
    private DashboardService dashboardService;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        return dashboardService.getCount();
    }
}
