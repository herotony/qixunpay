package com.qixunpay.Tools.hessianlite.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by saosinwork on 2017/9/20.
 */
abstract public class AbstractSerializer implements Serializer {

    protected static final Logger log
            = LoggerFactory.getLogger(AbstractSerializer.class.getName());

    abstract public void writeObject(Object obj, AbstractHessianOutput out)
            throws IOException;
}
