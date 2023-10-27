package com.liftoff.project.service;

public interface EncoderService {

    /**
     * Encodes the given text to a Base64 representation.
     *
     * @param text The text to be encoded to Base64.
     * @return The Base64-encoded representation of the input text.
     */
    String encodeToBase64(String text);

    /**
     * Decodes the given Base64-encoded text to its original form.
     *
     * @param encodedText The Base64-encoded text to be decoded.
     * @return The original text decoded from the Base64 representation.
     */
    String decodeBase64(String encodedText);

}
