package com.DelioCoder.cafe.controllers;

import com.DelioCoder.cafe.DTO.UserDTO;
import com.DelioCoder.cafe.POJO.User;
import com.DelioCoder.cafe.constant.CoffeConstants;
import com.DelioCoder.cafe.interfaces.UserRest;
import com.DelioCoder.cafe.interfaces.UserService;
import com.DelioCoder.cafe.services.UserServiceImpl;
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
public class AuthController implements UserRest {

    @Autowired
    UserServiceImpl userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try{

            return userService.signUp(requestMap);

        }catch(Exception ex) {
            ex.printStackTrace();
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> signIn(Map<String, String> requestMap) {
        try{

            return userService.signIn(requestMap);

        }catch(Exception ex) {
            ex.printStackTrace();
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUser() {
        log.info("inside getAllUser");
        try {

            return userService.getAllUser();

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ResponseEntity<List<UserDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateUser(Map<String, String> requestMap) {

        log.info("inside updateUserMethod");

        try {

            return userService.update(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> checkToken() {
        try{
            return userService.checkToken();
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {

        try{

            return userService.changePassword(requestMap);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
