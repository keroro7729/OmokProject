package com.example.OmokServer.Secure;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {

    private static final String KEY_PATH = "C:\\Users\\keror\\JINWOOK\\OmokProject\\OmokServer\\src\\main\\resources\\" +
                                            "keyfile";
    private static final String INFO_PATH = "C:\\Users\\keror\\JINWOOK\\OmokProject\\OmokServer\\src\\main\\resources\\" +
                                            "informations";
    private static final long DURATION = 60000;
    private static final long MONTH = 2592000000L;
    private static SecretKey secretKey;
    private static Map<Long, String> informations;

    public static void init(){
        File keyFile = new File(KEY_PATH);
        if (keyFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(keyFile))) {
                String encodedKey = reader.readLine();
                byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
                secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
            } catch (IOException e) {
                throw new RuntimeException("secretKey file error: "+e.getMessage());
            }
        } else generateAndStoreSecretKey();

        informations = new HashMap<>();
        File infoFile = new File(INFO_PATH);
        if (infoFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(infoFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        Long userId = Long.parseLong(parts[0]);
                        String deviceInfo = parts[1];
                        informations.put(userId, deviceInfo);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("informations file error: "+e.getMessage());
            }
        } else saveInformations();
    }
    public static AccessToken createAccessToken(Long userId){
        AccessToken token = new AccessToken();
        token.setId(userId);
        token.setExpirationTime(System.currentTimeMillis() + DURATION);
        token.setSignature(generateSignature(userId, token.getExpirationTime()));
        return token;
    }

    public static RefreshToken createRefreshToken(Long userId, DeviceInfo info){
        RefreshToken token = new RefreshToken();
        token.setId(userId);
        token.setExpirationTime(System.currentTimeMillis() + MONTH);
        info.setRandom(Math.random());
        saveInformations();
        token.setSignature(generateSignature(userId, info.toString()));
        return token;
    }

    public static boolean validateAccessToken(AccessToken token){
        long currentTime = System.currentTimeMillis();
        if (token.getExpirationTime() < currentTime)
            return false;
        String expectedSignature = generateSignature(token.getId(), token.getExpirationTime());
        return expectedSignature.equals(token.getSignature());
    }

    private static boolean validateRefreshToken(RefreshToken token){
        if(token.getExpirationTime() < System.currentTimeMillis())
            return false;
        String storedInfo = informations.get(token.getId());
        if (storedInfo == null)
            return false;
        String expectedSignature = generateSignature(token.getId(), storedInfo);
        return expectedSignature.equals(token.getSignature());
    }

    public static AccessToken refreshAccessToken(RefreshToken token){
        if(validateRefreshToken(token))
            return createAccessToken(token.getId());
        return null;
    }

    private static String generateSignature(Long userId, Object data){
        return generateSignature(userId+":"+data);
    }
    private static String generateSignature(String message){
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            byte[] hmac = mac.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(hmac);
        } catch (Exception e) {
            throw new RuntimeException("Signature 생성 오류", e);
        }
    }

    private static void generateAndStoreSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            secretKey = keyGen.generateKey();
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            try (FileWriter writer = new FileWriter(new File(KEY_PATH))) {
                writer.write(encodedKey);
            }
        } catch (Exception e) {
            throw new RuntimeException("key gen or file write error: "+e.getMessage());
        }
    }

    private static void saveInformations(){
        try (FileWriter writer = new FileWriter(new File(INFO_PATH))) {
            for (Map.Entry<Long, String> entry : informations.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("file write error: "+e.getMessage());
        }
    }
}
