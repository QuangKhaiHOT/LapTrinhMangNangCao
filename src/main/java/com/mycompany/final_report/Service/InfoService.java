/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report.Service;

/**
 * Start: 22/6/2025
 *
 * @author soniK
 */
import com.mycompany.final_report.Model.InfoEntity;
import com.mycompany.final_report.Repository.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class InfoService {

    @Autowired
    private InfoRepository infoRepository;

    public boolean uuidExists(String uuid) {
        return infoRepository.existsByUuid(uuid);
    }

    public boolean saveIfNotExist(String uuid, byte[] aes, byte[] iv) {
        if (infoRepository.existsByUuid(uuid)) {
            return false;
        }

        InfoEntity info = new InfoEntity();
        info.setUuid(uuid);
        info.setAes(aes);
        info.setIv(iv);

        infoRepository.save(info);
        return true;
    }
     public InfoEntity getInfoByUuid(String uuid) {
        Optional<InfoEntity> optional = infoRepository.findByUuid(uuid);
        return optional.orElse(null); // trả về null nếu không có
    }
}
