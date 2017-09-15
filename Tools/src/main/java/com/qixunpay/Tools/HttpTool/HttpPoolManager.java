package com.qixunpay.Tools.HttpTool;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by saosinwork on 2017/9/11.
 */
public class HttpPoolManager {

    private static Logger logger = LoggerFactory.getLogger(HttpPoolManager.class);

    private ConnectionSocketFactory plainSF = null;
    private LayeredConnectionSocketFactory sslSF = null;
    private PoolingHttpClientConnectionManager poolConnectionManager = null;
    private Registry<ConnectionSocketFactory> registry = null;
    private int maxConnectionCount = 3;
    private CloseableHttpClient httpClient = null;
    private RequestConfig requestConfig = null;
    private boolean bInit = false;
    private IdleConnectionMonitorThread idleConnectionMonitorThread;
    private  Thread monitorThread = null;

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public HttpPoolManager(String hostUrl, int hostPort,int maxConnectionCount){

        this.maxConnectionCount = maxConnectionCount;
        Init(hostUrl,hostPort);

        idleConnectionMonitorThread = new IdleConnectionMonitorThread(poolConnectionManager);
        //monitorThread = new Thread(idleConnectionMonitorThread);
        //monitorThread.start();
        logger.info("idle connection monitor thread start successfully");
    }

    private void Init(String hostUrl, int hostPort){

        if(bInit)
            return;

        try{
            requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(1000) //pick connection from pool.
                    .setConnectTimeout(3000)
                    .setSocketTimeout(6000)
                    .build();

            plainSF = PlainConnectionSocketFactory.getSocketFactory();
            sslSF = SSLConnectionSocketFactory.getSocketFactory();
            registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http",plainSF)
                    .register("https",sslSF)
                    .build();
            poolConnectionManager = new PoolingHttpClientConnectionManager(registry);
            poolConnectionManager.setMaxTotal(maxConnectionCount);
            poolConnectionManager.setDefaultMaxPerRoute(maxConnectionCount);
            HttpHost httpHost = new HttpHost(hostUrl,hostPort);
            poolConnectionManager.setMaxPerRoute(new HttpRoute(httpHost),maxConnectionCount);

            httpClient = HttpClients.custom().setConnectionManager(poolConnectionManager)
                    .setConnectionReuseStrategy(new DefaultClientConnectionReuseStrategy())
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            logger.info("init poolConnectionManager successfully");

        }catch(Exception initError){

            logger.error("init url:"+hostUrl+"[port:"+hostPort+"] occur error:"+initError);
        }

        bInit = true;
    }
}
