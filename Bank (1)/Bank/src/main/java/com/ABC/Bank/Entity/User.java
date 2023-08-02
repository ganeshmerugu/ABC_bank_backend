package com.ABC.Bank.Entity;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "signup",uniqueConstraints = @UniqueConstraint(columnNames = "user_name"))
@JsonPropertyOrder({"firstName","lastName","emailId","phoneNumber","userName","password"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;
    @Column(name = "user_name",unique = true)

    private String userName;
    private String password;



// Constructors, getters, and setters (omitted for brevity)


}

