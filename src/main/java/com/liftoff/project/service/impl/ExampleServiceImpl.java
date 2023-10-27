package com.liftoff.project.service.impl;

import com.liftoff.project.controller.example.response.ExampleResponse;
import com.liftoff.project.mapper.ExampleMapper;
import com.liftoff.project.repository.ExampleRepository;
import com.liftoff.project.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExampleServiceImpl implements ExampleService {

    private final ExampleRepository exampleRepository;
    private final ExampleMapper exampleMapper;

    @Autowired
    public ExampleServiceImpl(final ExampleRepository exampleRepository,
                              final ExampleMapper exampleMapper) {
        this.exampleRepository = exampleRepository;
        this.exampleMapper = exampleMapper;
    }

    @Override
    public List<ExampleResponse> readExampleData() {
        return exampleRepository.findAll().stream()
                .map(exampleMapper::mapEntityToResponse)
                .toList();
    }

}
