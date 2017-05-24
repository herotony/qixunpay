package com.qixunpay.Tools.AESTool;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by wangdalin(闪惠后台研发) on 2017/5/24 16:57.
 */
public class AESSecret {

    public static final String KEY_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private final String keyStr = "94327d4ffeab36fc89cb25427050fb2c";//由generatekey方法随机生成使用

    public byte[] encrypt(byte[] data)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        Key k = convertToSecretKey(hexStr2ByteArr(keyStr));
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, k);
        return cipher.doFinal(data);
    }

    public byte[] decrypt(byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Key k = convertToSecretKey(hexStr2ByteArr(keyStr));
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, k);
        return cipher.doFinal(data);
    }

    private Key convertToSecretKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        return secretKey;
    }

    public static byte[] generateKey() throws NoSuchAlgorithmException
    {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static String byteArr2HexStr(byte[] arrB) {
        int iLen = arrB.length;

        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];

            while (intTmp < 0) {
                intTmp += 256;
            }

            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    public static byte[] hexStr2ByteArr(String strIn) {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i += 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[(i / 2)] = (byte)Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    public static void main(String[] argv)
            throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        System.out.println(byteArr2HexStr(generateKey()));

        //加密举例
        String encryptStr = byteArr2HexStr(new AESSecret().encrypt("password".getBytes()));
        System.out.println("encrypt:"+encryptStr+" for:password");

        //揭秘举例
        String decryptStr = new String(new AESSecret().decrypt(hexStr2ByteArr(encryptStr)));
        System.out.println("decrypt:"+encryptStr+" get original-value:"+decryptStr);
    }

}
