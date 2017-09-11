package com.qixunpay.Tools.HttpTool;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by saosinwork on 2017/9/11.
 */
public class IdleConnectionMonitorThread extends  Thread{

    private static Logger logger = LoggerFactory.getLogger(IdleConnectionMonitorThread.class);
    private final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    private volatile boolean shutdown;
    public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager){

        super();
        shutdown=false;
        this.poolingHttpClientConnectionManager = poolingHttpClientConnectionManager;
    }

    public void run(){

        try{

            while(!shutdown){

                synchronized (this){

                    wait(1000);
                    poolingHttpClientConnectionManager.closeIdleConnections(20, TimeUnit.SECONDS);
                }
            }
        }catch(Exception monitorRunErr)
        {
            logger.error("monitor thread occur error:"+monitorRunErr);
        }
    }

    public void stopMonitor(){

        shutdown = true;
        synchronized (this){

            notifyAll();
        }
    }
}
