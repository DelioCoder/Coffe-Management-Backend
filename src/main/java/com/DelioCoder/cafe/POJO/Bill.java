package com.DelioCoder.cafe.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;

@NamedQuery(name = "Bill.getAllBills", query = "SELECT b from Bill b ORDER BY b.id desc")
@NamedQuery(name = "Bill.getBillByUsername", query = "SELECT b from Bill b WHERE b.createdBy =:username ORDER BY b.id desc")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "bill")
public class Bill implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column
    private String uuid;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private Integer contactNumber;

    @Column
    private String paymentMethod;

    @Column
    private Double total;

    @Column(name = "productDetails", columnDefinition = "json")
    private String productDetail;

    @Column
    private String createdBy;

}
