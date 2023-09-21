package com.liftoff.project.controller.example.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class ExampleResponse {

    private String firstName;

    private String lastName;

    private String career;
}
