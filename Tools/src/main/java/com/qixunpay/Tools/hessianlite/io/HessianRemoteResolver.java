package com.qixunpay.Tools.hessianlite.io;

import java.io.IOException;

/**
 * Created by saosinwork on 2017/9/20.
 */
public interface HessianRemoteResolver {

    public Object lookup(String type, String url)
            throws IOException;
}
