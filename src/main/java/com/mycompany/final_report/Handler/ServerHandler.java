/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report.Handler;

import com.mycompany.final_report.Model.InfoEntity;
import com.mycompany.final_report.Service.InfoService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;

/**
 * Start: 22/6/2025
 *
 * @author soniK
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private final InfoService infoService;
    private final String wordList = "subdomains.txt";

    public ServerHandler(InfoService infoService) {
        this.infoService = infoService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("Client mess: " + msg);
        JSONObject packet = new JSONObject(msg);
        String uuid = packet.getString("uuid");
        String mess = packet.getString("raw_message");
        byte[] sign = Base64.getDecoder().decode(packet.getString("signature"));
        byte[] encodePublickey = Base64.getDecoder().decode(packet.getString("encodePuclickey"));
        byte[] aes_byte, iv_byte;
        InfoEntity info = infoService.getInfoByUuid(uuid);
        if (info != null) {
            aes_byte = info.getAes();
            iv_byte = info.getIv();
        } else {
            aes_byte = Base64.getDecoder().decode(packet.getString("aes_key"));
            iv_byte = Base64.getDecoder().decode(packet.getString("iv_key"));
            boolean saved = infoService.saveIfNotExist(uuid, aes_byte, iv_byte);
            if (saved) {
                System.out.println("Saved new UUID");
            } else {
                System.out.println("UUID already exists");
            }
        }
        byte[] pubByte = decodePublickey(encodePublickey, aes_byte, iv_byte);
        PublicKey publickey = createPublickey(pubByte);
        if (verifySignature(publickey, mess, sign)) {
            System.out.println("Signature is valid");
            subdomainScan(mess, wordList, ctx);
        } else {
            System.out.println("Signature is invalid");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("New connect: " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Error: " + cause.getMessage());
        ctx.close();
    }

    private byte[] decodePublickey(byte[] key, byte[] aes, byte[] iv) throws Exception {
        SecretKey aesKey = new SecretKeySpec(aes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
        return aesCipher.doFinal(key);
    }

    private PublicKey createPublickey(byte[] key) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(key));
    }

    private boolean verifySignature(PublicKey pubKey, String mess, byte[] signature) throws Exception {
        Signature signatureVerifier = Signature.getInstance("SHA256withRSA");
        signatureVerifier.initVerify(pubKey);
        signatureVerifier.update(mess.getBytes());
        return signatureVerifier.verify(signature);
    }
    //Scan Subdomain

    public static void subdomainScan(String domain, String list, ChannelHandlerContext ctx) {
        ctx.writeAndFlush("*** Danh sach subdomain cua \" " + domain+" \" ***");
        try (BufferedReader br = new BufferedReader(new FileReader(list))) {
            String sub;
            while ((sub = br.readLine()) != null) {
                String fullDomain = sub.trim() + "." + domain;

                try {
                    InetAddress inetAddress = InetAddress.getByName(fullDomain);
                    String result = " - " + fullDomain;
                    ctx.writeAndFlush(result);
                } catch (UnknownHostException e) {
                    //Subdomain không tồn tại
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }
}
