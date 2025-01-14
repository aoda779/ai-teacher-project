package org.springbus.moban.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @Author hanjun
 * @Date 2024/04/16
 * @Description AES加密工具类
 * @Version 1.0
 */
@Slf4j
@Component
public class AESUtil {

    /**
     * 默认密钥的KEY
     */
    public static final String AES_KEY = "TRTCDESPHNUIKOHM";


    /**
     * 使用默认key加密字符串
     *
     * @param src
     * @return
     */
    public static String encrypt(String src) {
        return AESUtil.encrypt(src, AES_KEY);
    }


    /**
     * 使用默认key解密字符串
     *
     * @param src
     * @return
     */
    public static String decrypt(String src) {
        return AESUtil.decrypt(src, AES_KEY);
    }


    /**
     * 加密
     * 通过AES 加密成 byte[]
     * 然后将byte数组 转成 16进制的字符串
     *
     * @param src
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String src, String key) {
        if (StringUtils.isBlank(src) || StringUtils.isBlank(key)) {
            log.error("字符串或密钥不能为空");
            return null;
        }

        // 判断Key是否为16位
        if (key.length() != 16) {
            log.error("key长度不是16位");
            return null;
        }
        try {
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
            return encodeHexString(encrypted);
        } catch (Exception e) {
            log.error("加密失败,sourceData=" + src + "，error:", e);
            return null;
        }

    }


    /**
     * byte 数组转 16进制字符串
     *
     * @param byteArray byte数组
     * @return String
     */
    //遍历字节数组，使用 byteToHex 方法将每个字节转换为两位的十六进制字符，并追加到 StringBuilder 中。
    //最后返回构建好的十六进制字符串。
    public static String encodeHexString(byte[] byteArray) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (byte b : byteArray) {
            hexStringBuffer.append(byteToHex(b));
        }
        return hexStringBuffer.toString();
    }

    /**
     * byte 转16进制字符串
     *
     * @param num byte
     * @return String
     */
    //使用位运算提取高四位和低四位，并分别转换为十六进制字符。
    //返回由这两个字符组成的字符串。
    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }


    /**
     * 将16进制字符串转成二进制数组 然后解密
     *
     * @param src 16进制字符串
     * @param key 秘钥
     * @return
     */
    //解密逻辑:
    //将 key 转换为字节数组。
    //使用 SecretKeySpec 创建一个 AES 密钥规范对象。
    //获取指定算法（AES）、模式（ECB）和填充方式（PKCS5Padding）的 Cipher 实例。
    //初始化 Cipher 对象为解密模式。
    //使用 parseHexStr2Byte 方法将十六进制字符串转换为字节数组。
    //使用 doFinal 方法对加密后的字节数组进行解密，并将结果转换为原始字符串。
    public static String decrypt(String src, String key) {
        try {
            if (StringUtils.isBlank(src) || StringUtils.isBlank(src)){
                log.error("加密字符串或密钥不能为空");
                return null;
            }
            // 判断Key是否为16位
            if (key.length() != 16) {
                log.error("Key长度不是16位");
                return null;
            }
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = parseHexStr2Byte(src);
            //new BASE64Decoder().decodeBuffer(src);//先用base64解密
            if(Objects.isNull(encrypted1)){
                log.error("字符串解密失败");
                return null;
            }
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("字符串解密失败,sourceData="+src+",  error:",e);
            return null;
        }
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    //功能:
    //将十六进制字符串转换为字节数组。
    //实现:
    //检查输入字符串的长度是否小于 1，如果是，则返回 null。
    //创建一个长度为输入字符串长度一半的字节数组（因为每两个十六进制字符表示一个字节）。
    //遍历字符串，每次取两个字符，分别解析为高位和低位的整数值，并组合成一个字节。
    //将计算出的字节存入结果数组中，最后返回该数组。
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    public static void main(String[] args) {
        String str = "1680770916096188416";
        String encrypt = AESUtil.encrypt(str);
        System.out.println("加密字符串：" + encrypt);
        String decrypt = AESUtil.decrypt(encrypt);
        System.out.println("解密密字符串：" + decrypt);
    }


}