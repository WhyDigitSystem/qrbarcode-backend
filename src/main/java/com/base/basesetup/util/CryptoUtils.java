package com.base.basesetup.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class CryptoUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoUtils.class);
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";
    
    // Fetch these values from application properties
    private static final String SECRET_KEY = "u/Gu5posvwDsXUnV5Zaq4g==";  // Replace this with your key from properties
    private static final String INIT_VECTOR = "5D9r9ZVzEYYgha93/aUK2w==";  // Replace this with your IV from properties

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(INIT_VECTOR));
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), AES);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            LOGGER.error("Error while encrypting: " + ex.getMessage());
        }
        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(INIT_VECTOR));
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), AES);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            LOGGER.error("Error while decrypting: " + ex.getMessage());
        }

        return null;
    }
}
