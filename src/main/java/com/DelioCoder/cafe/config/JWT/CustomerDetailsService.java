package com.DelioCoder.cafe.config.JWT;

import com.DelioCoder.cafe.POJO.User;
import com.DelioCoder.cafe.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;


    private User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Dentro de loadUserByUsername {}", username);

        userDetail = userDao.findByEmail(username);

        if(!Objects.isNull(userDetail))
        {
            log.info("Usuario cargado: {}", userDetail);

            return new org.springframework.security.core.userdetails.User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        }
        else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

    }

    public User getUserDetail() { return userDetail; }

}
