package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
    private String name;
    private String email;
    private String password;
    private String address;
    private String phone_no;
    private String role;
}
