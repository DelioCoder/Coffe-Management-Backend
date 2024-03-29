package com.DelioCoder.cafe.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "User.findByEmail", query = "SELECT u from User u where u.email = :email")
@NamedQuery(name = "User.getAllUser", query = "SELECT new com.DelioCoder.cafe.DTO.UserDTO(u.id, u.name, u.email, u.contactNumber, u.status) from User u where u.role = 'user'")
@NamedQuery(name = "User.updateStatus", query = "UPDATE User u SET u.status = :status where u.id = :id")
@NamedQuery(name = "User.getAllAdmin", query = "SELECT u.email FROM User u WHERE u.role ='admin'")

@Data // Default constructor, Getters & Setters
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

}
