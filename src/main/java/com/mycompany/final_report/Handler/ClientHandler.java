/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_report.Handler;

import com.mycompany.final_report.Utility.KeyStorageUtil;
import com.mycompany.final_report.Utility.PemUtils;
import com.mycompany.final_report.Utility.UUIDManager;
import java.util.Scanner;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.json.JSONObject;

/**
 * Start: 22/6/2025
 *
 * @author soniK
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private ChannelHandlerContext ctx;
    Scanner scanner;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
            scanner = new Scanner(System.in);
            String choose;
                System.out.println("***==MENU==***");
                System.out.println("* 1. Scan    *");
                System.out.println("* 0. Exit    *");
                System.out.println("***========***");
                System.out.print("Your choose:");
                choose= scanner.nextLine();
                try {
                    switch (choose) {
                        case "1" -> {
                            String uuid = getUUID();
                            if (uuid != null) {
                                System.out.println("UUID: " + uuid);
                                String rawMess = getMess();
                                PrivateKey priKey = getPrivateKey();
                                byte[] digitalSign = SHA256withRSA(priKey, rawMess.getBytes(StandardCharsets.UTF_8));
                                byte[] pubKey = getPublicKey();//Doc publickey tu file
                                 SecretKey aes = KeyStorageUtil.loadAESKey();//Load AES tu file
                                byte[] aesByte = aes.getEncoded();//
                                IvParameterSpec iv =KeyStorageUtil.loadIV();//Load IV tu file
                                byte[] ivByte = iv.getIV();
                                 // Mã hóa public key bằng AES-CBC
                                byte[] encryptedPub=encodePublickey(aes, iv, pubKey);
                                String json = getJson(rawMess, uuid, digitalSign, encryptedPub, aesByte, ivByte).toString();
                                ctx.writeAndFlush(json);
                            } else {
                                UUIDManager.createUUID();
                                uuid = getUUID();
                                System.out.println("New UUID: " + uuid);
                                String rawMess = getMess();
                                PrivateKey priKey = getPrivateKey();
                                byte[] digitalSign = SHA256withRSA(priKey, rawMess.getBytes(StandardCharsets.UTF_8));
                                byte[] pubKey = getPublicKey();//Doc publickey tu file
                                SecretKey aes = createAES();//Tao AES key
                                byte[] aesByte = aes.getEncoded();//
                                IvParameterSpec iv = createIV();//Tao IV
                                byte[] ivByte = iv.getIV();
                                //Luu AES IV vao file
                                KeyStorageUtil.saveAESKeyAndIV(aesByte, ivByte);
                                 // Mã hóa public key bằng AES-CBC
                                byte[] encryptedPub=encodePublickey(aes, iv, pubKey);
                                String json = getJson(rawMess, uuid, digitalSign, encryptedPub, aesByte, ivByte).toString();
                                ctx.writeAndFlush(json);
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg
    ) {
        System.out.println(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause
    ) {
        System.out.println("Error client: " + cause.getMessage());
        ctx.close();
    }

    //Tao raw mess
    private String getMess() {
        System.out.print("Raw - Message :");
        return scanner.nextLine().trim();
    }
//Đọc PrivateKey

    private PrivateKey getPrivateKey() throws Exception {
        return PemUtils.readPrivateKey("private_key.pem", "RSA");
    }
//Ký số (SHA256withRSA)

    private byte[] SHA256withRSA(PrivateKey key, byte[] mess) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(key);
        signature.update(mess);
        return signature.sign();
    }
//Đọc PublicKey

    private byte[] getPublicKey() throws Exception {
        return PemUtils.readPublicKey("public_key.pem", "RSA");
    }
//Tạo AES key

    private SecretKey createAES() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // AES-128
        return keyGen.generateKey();
    }
//Tạo IV

    private IvParameterSpec createIV() {
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }
// Mã hóa public key bằng AES-CBC

    private byte[] encodePublickey(SecretKey aes, IvParameterSpec iv, byte[] key) throws Exception {
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, aes, iv);
        return aesCipher.doFinal(key);
    }
//Tạo JSON object

    private JSONObject getJson(String mess, String uuid, byte[] sign, byte[] key, byte[] aes, byte[] iv) throws Exception {
        JSONObject packet = new JSONObject();
        packet.put("uuid", uuid);
        packet.put("raw_message", mess);
        packet.put("signature", Base64.getEncoder().encodeToString(sign));
        packet.put("encodePuclickey", Base64.getEncoder().encodeToString(key));
        if (aes != null) {
            packet.put("aes_key", Base64.getEncoder().encodeToString(aes));
        }
        if (iv != null) {
            packet.put("iv_key", Base64.getEncoder().encodeToString(iv));
        }
        return packet;
    }
//Lay UUID

    private String getUUID() {
        if (UUIDManager.uuidFileExists()) {
            return UUIDManager.readUUIDFromFile();
        }
        return null;
    }
}
