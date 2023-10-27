package com.liftoff.project.service.impl;

import com.liftoff.project.service.EncoderService;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class EncoderServiceImpl implements EncoderService {
    @Override
    public String encodeToBase64(String text) {
        byte[] textBytes = text.getBytes();

        return Base64.getEncoder().encodeToString(textBytes);
    }

    @Override
    public String decodeBase64(String encodedText) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);

        return new String(decodedBytes);
    }

}