package com.qixunpay.Tools.hessianlite.io;

import java.io.IOException;

/**
 * Created by saosinwork on 2017/9/20.
 */
public interface Deserializer {

    public Class getType();

    public Object readObject(AbstractHessianInput in)
            throws IOException;

    public Object readList(AbstractHessianInput in, int length)
            throws IOException;

    public Object readLengthList(AbstractHessianInput in, int length)
            throws IOException;

    public Object readMap(AbstractHessianInput in)
            throws IOException;

    public Object readObject(AbstractHessianInput in, String[] fieldNames)
            throws IOException;

}
