package com.liftoff.project.controller;

import com.liftoff.project.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/example")
@CrossOrigin("*")
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
