/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report.Utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

/**
 * Start: 22/6/2025
 *
 * @author soniK
 */
public class UUIDManager {
    private static final String FILE_NAME = ".my_uuid"; // tên file UUID
    private static final String FILE_PATH = System.getProperty("user.home") + File.separator + FILE_NAME;
    
    // Hàm tạo UUID và lưu vào file
    public static void createUUID(){
         String uuid = UUID.randomUUID().toString();
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(uuid);
        } catch (IOException e) {
           e.getMessage();
        }
    }
    // Hàm kiểm tra xem file UUID đã tồn tại hay chưa
    public static boolean uuidFileExists() {
        File file = new File(FILE_PATH);
        return file.exists();
    }
    // (Tuỳ chọn) Hàm đọc UUID từ file nếu cần
    public static String readUUIDFromFile() {
        try {
            return Files.readString(new File(FILE_PATH).toPath());
        } catch (IOException e) {
            return null;
        }
    }
}
