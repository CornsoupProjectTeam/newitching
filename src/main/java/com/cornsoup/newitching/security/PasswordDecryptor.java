package com.cornsoup.newitching.security;

public interface PasswordDecryptor {
    String decrypt(String encryptedPassword);
}