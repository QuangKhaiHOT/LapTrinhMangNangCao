/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report.Model;

import jakarta.persistence.*; 
/**
 * Start: 22/6/2025
 *
 * @author soniK
 */
@Entity
@Table(name = "tb_info") // tên bảng trong db_server
public class InfoEntity {
    @Id
    private String uuid;

    @Lob // lưu dữ liệu binary lớn
    private byte[] aes;

    @Lob
    private byte[] iv;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public byte[] getAes() {
        return aes;
    }

    public void setAes(byte[] aes) {
        this.aes = aes;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }
}
