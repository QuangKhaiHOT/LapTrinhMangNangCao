/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report.Repository;
/**
 * Start: 22/6/2025
 *
 * @author soniK
 */
import com.mycompany.final_report.Model.InfoEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoRepository extends JpaRepository<InfoEntity, String> {
    boolean existsByUuid(String uuid); // kiểm tra uuid tồn tại chưa
    Optional<InfoEntity> findByUuid(String uuid);//Tim uuid
}
