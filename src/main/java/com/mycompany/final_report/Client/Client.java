/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
/**
 * Start: 22/6/2025
 *
 * @author soniK
 */
public class Client {

    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap cl = new Bootstrap(); // client bootstrap
            cl.group(group)
                    .channel(NioSocketChannel.class) // dùng NIO
                    .handler(new ClientInitializer(host, port));

           // Kết nối đến server
            ChannelFuture future = cl.connect(host, port).sync();
            // Chờ cho đến khi đóng kết nối
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception{
       new Client("localhost",8888).start();
    }
}
