package com.DelioCoder.cafe.dao;

import com.DelioCoder.cafe.DTO.UserDTO;
import com.DelioCoder.cafe.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer>
{

    public User findByEmail(@Param("email") String email);


    public List<UserDTO> getAllUser();

}
