/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report;

import com.mycompany.final_report.Server.Server;
import com.mycompany.final_report.Service.InfoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Start: 22/6/2025
 *
 * @author soniK
 */
@SpringBootApplication
public class FinalReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalReportApplication.class, args);
    }

    @Bean
    public CommandLineRunner runServer(InfoService infoService) {
        return args -> {
            new Server(8888, infoService).start(); // gọi server Netty với InfoService
        };
    }
}