package com.example.OmokServer.Secure;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Base64;

public class EncryptUtil {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16; // AES 블록 크기
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    static {
        // Bouncy Castle 프로바이더 추가
        Security.addProvider(new BouncyCastleProvider());
    }

    public static SecretKey generateSecretKey(){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // 128, 192, 256 비트 키 중 선택
            return keyGen.generateKey();
        }catch (Exception e){
            throw new RuntimeException("Failed to generate secret key: "+e.getMessage());
        }
    }

    public static String encrypt(String data, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] iv = new byte[IV_SIZE];
            new java.security.SecureRandom().nextBytes(iv);
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);

            byte[] encrypted = cipher.doFinal(data.getBytes());

            // IV와 암호문을 Base64로 인코딩하여 반환
            return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(encrypted);
        }catch (Exception e){
            throw new RuntimeException("Failed to encrypt data: "+e.getMessage());
        }
    }

    public static String decrypt(String encryptedData, SecretKey secretKey) {
        try {
            String[] parts = encryptedData.split(":");
            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] encrypted = Base64.getDecoder().decode(parts[1]);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);

            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data: "+e.getMessage());
        }
    }

    public static String secretKeyToString(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static SecretKey stringToSecretKey(String keyString) {
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}