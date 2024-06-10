package com.example.bookmark.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A very insecure MD5 password encoder.
 * Please DO NOT use this anywhere in production !!!!!!!!
 */
public class MD5PasswordEncoder implements PasswordEncoder {

    private static final Logger LOG = LoggerFactory.getLogger(MD5PasswordEncoder.class);

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new String(Hex.encode(md.digest(rawPassword.toString().getBytes(UTF_8))));
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("Error in encoding", e);
            return null;
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String rawPasswordEncoded = new String(Hex.encode(md.digest(rawPassword.toString().getBytes(UTF_8))));
            return digestEquals(encodedPassword, rawPasswordEncoded);
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("Error in matching", e);
            return false;
        }
    }

    private boolean digestEquals(String expected, String actual) {
        byte[] expectedBytes = bytesUtf8(expected);
        byte[] actualBytes = bytesUtf8(actual);
        return MessageDigest.isEqual(expectedBytes, actualBytes);
    }

    private byte[] bytesUtf8(String s) {
        return (s != null) ? Utf8.encode(s) : null;
    }
}
