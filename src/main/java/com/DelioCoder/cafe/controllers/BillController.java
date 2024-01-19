package com.DelioCoder.cafe.controllers;

import com.DelioCoder.cafe.POJO.Bill;
import com.DelioCoder.cafe.constant.CoffeConstants;
import com.DelioCoder.cafe.interfaces.Bill.BillRest;
import com.DelioCoder.cafe.services.BillServiceImpl;
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
public class BillController implements BillRest {

    @Autowired
    BillServiceImpl billService;

    @Override
    public ResponseEntity<List<Bill>> getBills() {

        try {

            return billService.retrieveBills();

        }catch (Exception ex) {
            log.error("BillController - Error: ", ex);
        }

        return new ResponseEntity<List<Bill>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {

        try {

            return billService.getPdf(requestMap);

        }catch (Exception ex)
        {
            log.error("BillController - Error: ", ex);
        }

        return null;

    }

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {

        try {

            return billService.generateReport(requestMap);

        }catch (Exception ex)
        {
            log.error("BillController - Error: " + ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {

        try {

            return billService.deleteBill(id);

        }catch (Exception ex)
        {
            log.error("BillController - Error: ", ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
