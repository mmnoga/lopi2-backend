package com.liftoff.project.controller.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Data
//@Builder(setterPrefix = "with")
public class JwtResponseDTO {

    private String token;
    private String type = "Bearer";
    //private String refreshToken; // we are still waiting for implementation of RefreshToken
    private String username;
    private String email;
    private List<String> roles;
    private String firstName;
    private String lastName;
    private UUID uuid;


    public JwtResponseDTO(String token, String type, UUID uuid, String username, String email, List<String> roles, String firstName, String lastName) {
        this.token = token;
        //this.refreshToken = refreshToken;
        this.type = "Bearer";
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;


    }

}
