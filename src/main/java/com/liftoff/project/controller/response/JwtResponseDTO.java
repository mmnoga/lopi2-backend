package com.liftoff.project.controller.response;

import lombok.Data;

@Data
public class JwtResponseDTO {

    private String token;
    private String username;
    private String role;
    private String firstName;
    private String lastName;


    public JwtResponseDTO(String token, String username, String role, String firstName, String lastName) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;


    }

}
