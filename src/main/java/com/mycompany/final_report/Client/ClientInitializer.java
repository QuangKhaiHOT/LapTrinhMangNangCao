/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report.Client;

import com.mycompany.final_report.Handler.ClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import javax.net.ssl.SSLException;

/**
 *
 * @author ASUS
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private final String host;
    private final int port;

    public ClientInitializer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws SSLException {
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE) // chấp nhận cert tự ký (DEV only)
                .build();

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addFirst(sslContext.newHandler(ch.alloc(), host, port));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new ClientHandler());
    }
}
