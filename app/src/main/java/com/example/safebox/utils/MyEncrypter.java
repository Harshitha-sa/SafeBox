package com.example.safebox.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MyEncrypter {
    private final static int READ_WRITE_BLOCK_BUFFER=1024;
    private final static String ALGO_IMAGE_ENCRYPTOR="AES/CBC/PKCS5Padding";
    public final static String ALGO_SECRET_KEY="AES";


    public static void encryptToFile(String keyStr,String specStr,
                                     InputStream inputStream,
                                     OutputStream outputStream)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            IOException {
        try {
            IvParameterSpec ivParameterSpec=new IvParameterSpec(
                    specStr.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec=new SecretKeySpec(keyStr.getBytes(
                    "UTF-8"),ALGO_SECRET_KEY);
            Cipher cipher=Cipher.getInstance(ALGO_IMAGE_ENCRYPTOR);
            cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ivParameterSpec);
            outputStream=new CipherOutputStream(outputStream,cipher);
            int count=0;
            byte[] buffer=new byte[READ_WRITE_BLOCK_BUFFER];
            while ((count=inputStream.read(buffer))>0){
                outputStream.write(buffer,0,count);
            }
        }
        finally {
            outputStream.close();
        }
    }

    public static void decryptToFile(String keyStr,String specStr,
                                     InputStream inputStream,
                                     OutputStream outputStream)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            IOException {
        try {
            IvParameterSpec ivParameterSpec=new IvParameterSpec(
                    specStr.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec=new SecretKeySpec(keyStr.getBytes(
                    "UTF-8"),ALGO_SECRET_KEY);
            Cipher cipher=Cipher.getInstance(ALGO_IMAGE_ENCRYPTOR);
            cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,ivParameterSpec);
            outputStream=new CipherOutputStream(outputStream,cipher);
            int count=0;
            byte[] buffer=new byte[READ_WRITE_BLOCK_BUFFER];
            while ((count=inputStream.read(buffer))>0){
                outputStream.write(buffer,0,count);
            }
        }
        finally {
            outputStream.close();
        }
    }
}
