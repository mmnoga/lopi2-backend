package com.liftoff.project.command;

import com.liftoff.project.repository.ExampleRepository;
import jakarta.annotation.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Priority(2)
@Profile("dev")
public class ExampleReadCommand implements CommandLineRunner {

    private final ExampleRepository exampleRepository;

    @Autowired
    public ExampleReadCommand(final ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        exampleRepository.findAll()
                .forEach(example -> System.out.println(example.toString()));
    }
}
