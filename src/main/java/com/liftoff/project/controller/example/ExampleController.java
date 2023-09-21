package com.liftoff.project.controller.example;

import com.liftoff.project.service.ExampleService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
@Hidden
public class ExampleController {

    private final ExampleService exampleService;

    @Autowired
    public ExampleController(final ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("/read")
    public ResponseEntity<?> getTestData(){
        return ResponseEntity.ok(exampleService.readExampleData());
    }
}
