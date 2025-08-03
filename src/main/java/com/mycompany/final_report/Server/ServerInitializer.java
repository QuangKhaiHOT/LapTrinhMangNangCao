/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report.Server;

import com.mycompany.final_report.Handler.ServerHandler;
import com.mycompany.final_report.Service.InfoService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import java.io.File;

/**
 *
 * @author ASUS
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final InfoService infoService;

    public ServerInitializer(InfoService infoService) {
        this.infoService = infoService;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        File cert = new File("server.crt");
        File key = new File("server.key");

        SslContext sslContext = SslContextBuilder.forServer(cert, key).build();

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addFirst(sslContext.newHandler(ch.alloc()));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new ServerHandler(infoService));
    }
}
