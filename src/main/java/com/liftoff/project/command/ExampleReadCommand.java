package com.liftoff.project.command;

import com.liftoff.project.configuration.DevProfile;
import com.liftoff.project.configuration.jwt.AuthTokenFilter;
import com.liftoff.project.repository.ExampleRepository;
import jakarta.annotation.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@Priority(2)
@DevProfile
public class ExampleReadCommand implements CommandLineRunner {

    private final ExampleRepository exampleRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    public ExampleReadCommand(final ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        exampleRepository.findAll()
                .forEach(example -> LOGGER.info(example.toString()));
    }
}
