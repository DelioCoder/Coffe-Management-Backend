package com.DelioCoder.cafe.interfaces;

import com.DelioCoder.cafe.DTO.UserDTO;
import com.DelioCoder.cafe.POJO.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/user")
public interface UserRest
{
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<UserDTO>> getAllUser();

}
