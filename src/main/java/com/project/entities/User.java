package com.project.entities;

import lombok.*;

import javax.persistence.*;


@Data
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private String password;
    private String address;
    private String phone_no;
    private String role;

    public User(String name, String email, String password, String address, String phone_no, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone_no = phone_no;
        this.role = role;
    }
}
