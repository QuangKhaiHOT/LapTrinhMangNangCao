/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report.Server;

import com.mycompany.final_report.Service.InfoService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Start: 22/6/2025
 *
 * @author soniK
 */
public class Server {

    private final int port;
    private final InfoService infoService;

    public Server(int port, InfoService infoService) {
        this.port = port;
        this.infoService = infoService;
    }

    public void start() throws Exception {
        //BossGroup: chấp nhận kết nối mới
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //WorkerGroup: xử lý dữ liệu I/O của client
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //Cau hinh server
            ServerBootstrap sv = new ServerBootstrap();
            sv.group(bossGroup, workerGroup)//Gan nhom xu ly
                    .channel(NioServerSocketChannel.class)//Su dung NIO cho socket server
                    .childHandler(new ServerInitializer(infoService));

            ChannelFuture f = sv.bind(port).sync(); // gán cổng và khởi động
            System.out.println("Server started on port " + port);

            f.channel().closeFuture().sync(); // chờ khi server tắt
        } finally {
            bossGroup.shutdownGracefully(); // tắt nhóm
            workerGroup.shutdownGracefully();
        }
    }
}
