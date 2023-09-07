package com.liftoff.project.configuration;

import com.google.firebase.FirebaseApp;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

@TestConfiguration
public class FirebaseMockConfig {

    @Bean
    @Primary
    public FirebaseConfig firebaseConfig() throws IOException {
        FirebaseConfig firebaseConfig = new FirebaseConfig();

        FirebaseApp mockFirebaseApp = Mockito.mock(FirebaseApp.class);
        Mockito.when(mockFirebaseApp.getName()).thenReturn("DEFAULT");
        Mockito.when(firebaseConfig.firebaseApp()).thenReturn(mockFirebaseApp);

        return firebaseConfig;
    }

}
