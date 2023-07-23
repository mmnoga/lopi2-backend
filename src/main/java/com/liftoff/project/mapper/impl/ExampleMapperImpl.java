package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.response.ExampleResponse;
import com.liftoff.project.mapper.ExampleMapper;
import com.liftoff.project.model.Example;
import org.springframework.stereotype.Component;

@Component
public class ExampleMapperImpl implements ExampleMapper {


    @Override
    public ExampleResponse mapEntityToResponse(Example example) {

        return ExampleResponse.builder()
                .withFirstName(example.getFirstName())
                .withLastName(example.getLastName())
                .withCareer(example.getCareer())
                .build();
    }
}
