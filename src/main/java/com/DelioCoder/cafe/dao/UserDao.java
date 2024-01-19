package com.DelioCoder.cafe.dao;

import com.DelioCoder.cafe.DTO.UserDTO;
import com.DelioCoder.cafe.POJO.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer>
{

    public User findByEmail(@Param("email") String email);

    public List<UserDTO> getAllUser();

    public List<String> getAllAdmin();

    @Transactional
    @Modifying
    public Integer updateStatus(@Param("status") String status, @Param("id") Integer id);


}
