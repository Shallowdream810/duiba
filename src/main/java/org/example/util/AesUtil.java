package org.example.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {
        private static final String key = "aesEncryptionKey";
        private static final String initVector = "encryptionIntVec";

        //加密
        public static String encrypt(String value) {
            try {
                IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

                byte[] encrypted = cipher.doFinal(value.getBytes());
                return Base64.encodeBase64String(encrypted);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        //    解密
        public static String decrypt(String encrypted) {
            try {
                IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
                byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
                return new String(original);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        //主方法
        public static void main(String[] args) {
            String originalString = "测试数据";
            System.out.println("要加密数据" + originalString);
            String encryptedString = encrypt(originalString);
            System.out.println("加密后" + encryptedString);
            String decryptedString = decrypt(encryptedString);
            System.out.println("解密" + decryptedString);
        }
}
