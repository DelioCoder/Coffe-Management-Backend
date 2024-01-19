package com.DelioCoder.cafe.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Date;

@Slf4j
public class CoffeUtils {

    private CoffeUtils() { }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<>(responseMessage, httpStatus);
    }

    public static String getUUID()
    {
        Date date = new Date();

        long time = date.getTime();
        return "BILL-" + time;
    }

    public static JSONArray getJsonArrayFromString(String data) throws JSONException
    {
        return new JSONArray(data);
    }

    public static Boolean isFileExist(String path)
    {
        log.info("Inside isFileExist {}", path);

        try {

            File file = new File(path);

            return file.exists() ? Boolean.TRUE : Boolean.FALSE;

        }catch (Exception ex)
        {
            log.error("CoffeUtils - Error: ", ex);
        }

        return false;
    }

}
