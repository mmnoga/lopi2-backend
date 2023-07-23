package com.liftoff.project.mapper;

import com.liftoff.project.controller.response.ExampleResponse;
import com.liftoff.project.model.Example;

public interface ExampleMapper {

    ExampleResponse mapEntityToResponse(Example example);
}
