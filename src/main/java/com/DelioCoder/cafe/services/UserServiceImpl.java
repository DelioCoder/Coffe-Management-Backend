package com.DelioCoder.cafe.services;

import com.DelioCoder.cafe.DTO.UserDTO;
import com.DelioCoder.cafe.POJO.User;
import com.DelioCoder.cafe.config.JWT.CustomerDetailsService;
import com.DelioCoder.cafe.config.JWT.JwtFilter;
import com.DelioCoder.cafe.config.JWT.JwtUtil;
import com.DelioCoder.cafe.constant.CoffeConstants;
import com.DelioCoder.cafe.dao.UserDao;
import com.DelioCoder.cafe.interfaces.UserService;
import com.DelioCoder.cafe.utils.CoffeUtils;
import com.DelioCoder.cafe.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {

        log.info("Inside signup {}", requestMap);

        try{

            if(validateSignUpMap(requestMap))
            {
                User user = userDao.findByEmail(requestMap.get("email"));

                if(Objects.isNull(user)){

                    User newUser = getUserFromMap(requestMap);

                    newUser.setPassword(encodePassword(requestMap.get("password")));

                    userDao.save(newUser);

                    return CoffeUtils.getResponseEntity("Successfully Registered", HttpStatus.CREATED);
                }else {
                    return CoffeUtils.getResponseEntity("Email already exist.", HttpStatus.BAD_REQUEST);
                }
            }else {
                return CoffeUtils.getResponseEntity(CoffeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> signIn(Map<String, String> requestMap) {

        log.info("Dentro de login");

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );

            if(authentication.isAuthenticated()){
                if(customerDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return CoffeUtils
                            .getResponseEntity(
                                    "Token: " +
                                            jwtUtil.generateToken(
                                                    customerDetailsService.getUserDetail().getEmail(),
                                                    customerDetailsService.getUserDetail().getRole()),
                                    HttpStatus.OK);
                }else {
                    return CoffeUtils.getResponseEntity("Espera a la aprobación del administrador.", HttpStatus.BAD_REQUEST);
                }
            }

        }catch (Exception ex){
            log.error("{}", ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUser() {
        log.info("inside getAllUserService");
        try {

            if(this.jwtFilter.isAdmin())
            {
                return new ResponseEntity<>(new ArrayList<>(userDao.getAllUser()), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {

        try{

            if(jwtFilter.isAdmin())
            {

                Optional<User> user = userDao.findById(Integer.parseInt(requestMap.get("id")));

                if(user.isPresent()){
                    userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));

                    sendEmailToAllAdmin(requestMap.get("status"), user.get().getEmail(), userDao.getAllAdmin());

                    return CoffeUtils.getResponseEntity("User Status Updated Successfully", HttpStatus.OK);
                }else {
                    return CoffeUtils.getResponseEntity(CoffeConstants.USER_NOT_FOUND, HttpStatus.OK);
                }

            }else {
                return CoffeUtils.getResponseEntity(CoffeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {

        return CoffeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {

        try{

            User user = userDao.findByEmail(jwtFilter.getCurrentUser());

            if(user != null){
                if(passwordEncoder.matches(requestMap.get("oldPassword"), user.getPassword())){
                    user.setPassword(passwordEncoder.encode(requestMap.get("newPassword")));
                    userDao.save((user));

                    return CoffeUtils.getResponseEntity("Password updated Successfully", HttpStatus.OK);
                }

                return CoffeUtils.getResponseEntity("Incorrect Old password", HttpStatus.BAD_REQUEST);
            }

            return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendEmailToAllAdmin(String status, String user, List<String> allAdmin) {

        allAdmin.remove(jwtFilter.getCurrentUser());

        if(status != null && status.equalsIgnoreCase("true"))
        {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account approved", "USER:- " + user + "\n is approved by \nADMIN:- " + jwtFilter.getCurrentUser(), allAdmin);
        }else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account disabled", "USER:- " + user + "\n is disabled by \nADMIN:- " + jwtFilter.getCurrentUser(), allAdmin);
        }

    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }else {
            return false;
        }
    }

    private User getUserFromMap(Map<String, String> requestMap)
    {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
