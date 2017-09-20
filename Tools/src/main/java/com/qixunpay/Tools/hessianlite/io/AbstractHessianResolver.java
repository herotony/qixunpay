package com.qixunpay.Tools.hessianlite.io;

/**
 * Created by saosinwork on 2017/9/20.
 */
public class AbstractHessianResolver implements HessianRemoteResolver {

    public Object lookup(String type,String url){
        return new HessianRemote(type,url);
    }
}
