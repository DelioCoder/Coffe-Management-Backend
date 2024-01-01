package com.DelioCoder.cafe.interfaces;

import com.DelioCoder.cafe.DTO.UserDTO;
import com.DelioCoder.cafe.POJO.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface UserService {

    ResponseEntity<String> signUp(Map<String, String> requestMap);

    ResponseEntity<String> signIn(Map<String, String> requestMap);

    ResponseEntity<List<UserDTO>> getAllUser();

}
