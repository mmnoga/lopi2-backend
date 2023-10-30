package com.liftoff.project.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EncoderServiceImplTest {

    @InjectMocks
    private EncoderServiceImpl encoderService;

    @Test
    void shouldEncodeTextToBase64() {
        // given
        String text = "Hello, World!";
        byte[] encodedBytes = Base64.getEncoder().encode(text.getBytes());
        String encodedText = new String(encodedBytes);

        // when
        String result = encoderService.encodeToBase64(text);

        // then
        assertEquals(encodedText, result);
    }

    @Test
    void shouldDecodeEncodedBase64Text() {
        // given
        String encodedText = "SGVsbG8sIFdvcmxkIQ==";
        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
        String decodedText = new String(decodedBytes);

        // when
        String result = encoderService.decodeBase64(encodedText);

        // then
        assertEquals(decodedText, result);
    }

}