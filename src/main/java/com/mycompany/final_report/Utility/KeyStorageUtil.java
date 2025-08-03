/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report.Utility;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Start: 22/6/2025
 *
 * @author soniK
 */
public class KeyStorageUtil {

    private static final String HOME_DIR = System.getProperty("user.home");

    //Luu data vao file
    private static void saveToFile(String fileName, byte[] data) throws Exception {
        String base64 = Base64.getEncoder().encodeToString(data);
        Path filePath = Paths.get(HOME_DIR, fileName);
        Files.write(filePath, base64.getBytes());
    }
    //Luu AES va IV

    public static void saveAESKeyAndIV(byte[] aesKeyBytes, byte[] ivBytes) throws Exception {
        saveToFile("aes.key", aesKeyBytes);
        saveToFile("iv.key", ivBytes);
    }
    
    //Doc data tu file
    private static byte[] readFromFile(String fileName) throws Exception {
        Path filePath = Paths.get(HOME_DIR, fileName);
        String base64 = Files.readString(filePath);
        return Base64.getDecoder().decode(base64);
    }

    //Load AES key từ file và tạo lại SecretKey
    public static SecretKey loadAESKey() throws Exception {
        byte[] keyBytes = readFromFile("aes.key");
        return new SecretKeySpec(keyBytes, "AES");
    }

    // Load IV từ file và tạo lại IvParameterSpec
    public static IvParameterSpec loadIV() throws Exception {
        byte[] ivBytes = readFromFile("iv.key");
        return new IvParameterSpec(ivBytes);
    }
}
