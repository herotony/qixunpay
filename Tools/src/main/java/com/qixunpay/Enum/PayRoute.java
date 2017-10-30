package com.qixunpay.Enum;

import com.qixunpay.Tools.AESTool.AESSecret;

import java.io.UnsupportedEncodingException;

/**
 * Created by saosinwork on 2017/10/25.
 */
public enum PayRoute {

    PAYINSTANCE;

    private String u1;
    private String u2;
    private AESSecret aesSecret;

    private PayRoute(){
        u1 = "u are singleton";
        u2 = "we are singleton";
        aesSecret = new AESSecret();
    }

    //最合理高效安全的单例模式，即PayRoute[单一实例类].PAYINSTANCE.Pay()方法来提供相关功能
    public void Pay() throws Exception{
        System.out.println("start pay "+u1+" "+u2);
        String encrypt = AESSecret.byteArr2HexStr( aesSecret.encrypt("测试".getBytes("utf-8")));
        System.out.println("encrypt:"+encrypt);
        System.out.println("decrypt:"+ new String(aesSecret.decrypt(AESSecret.hexStr2ByteArr(encrypt))) );
    }

}
