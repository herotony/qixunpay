package com.qixunpay.Tools.hessianlite.io;

import java.io.IOException;

/**
 * Created by saosinwork on 2017/9/20.
 */
public interface Serializer {

    public void writeObject(Object object,AbstractHessianOutput out) throws IOException;
}
