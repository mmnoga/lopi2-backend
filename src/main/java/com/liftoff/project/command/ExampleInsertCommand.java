package com.liftoff.project.command;
import com.liftoff.project.model.Example;
import com.liftoff.project.repository.ExampleRepository;
import jakarta.annotation.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Priority(1)
@Profile("!prod")
public class ExampleInsertCommand implements CommandLineRunner {

    private final ExampleRepository exampleRepository;

    @Autowired
    public ExampleInsertCommand(final ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        exampleRepository.save(Example.builder()
                .withFirstName("Artur")
                .withLastName("Dudzik")
                .withCareer("fullstack dev")
                .build());
    }
}
