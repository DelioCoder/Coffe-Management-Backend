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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {

        log.info("Inside signup {}", requestMap);

        try{

            if(validateSignUpMap(requestMap))
            {
                User user = userDao.findByEmail(requestMap.get("email"));

                if(Objects.isNull(user)){
                    userDao.save(getUserFromMap(requestMap));

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
        log.info("Dentro de get");
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

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
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

}
