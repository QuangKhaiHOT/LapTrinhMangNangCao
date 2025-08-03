/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report.Utility;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * Start: 22/6/2025
 *
 * @author soniK
 */
public class PemUtils {
    public static PrivateKey readPrivateKey(String fileName, String algorithm) throws Exception{
        // Loại bỏ phần header/footer và xuống dòng
        String key = new String(Files.readAllBytes(Paths.get(fileName)))
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", ""); // Xoá dòng trắng, dấu xuống dòng

        // Giải mã BASE64 → byte[]
        byte[] decoded = Base64.getDecoder().decode(key);

        // Tạo đối tượng PrivateKey
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(spec);
    }
    public static byte[] readPublicKey(String fileName, String algorithm) throws Exception{
        // Loại bỏ phần header/footer và xuống dòng
        String key = new String(Files.readAllBytes(Paths.get(fileName)))
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", ""); // Xoá dòng trắng, dấu xuống dòng

        // Giải mã BASE64 → byte[]
        byte[] decoded = Base64.getDecoder().decode(key);
        return decoded;
    }
}
