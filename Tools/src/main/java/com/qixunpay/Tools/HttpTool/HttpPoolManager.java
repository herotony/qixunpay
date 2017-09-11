package com.qixunpay.Tools.HttpTool;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
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
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Created by saosinwork on 2017/9/11.
 */
public class HttpPoolManager {

    private static Logger logger = LoggerFactory.getLogger(HttpPoolManager.class);

    private ConnectionSocketFactory plainSF = null;
    private LayeredConnectionSocketFactory sslSF = null;
    private PoolingHttpClientConnectionManager poolConnectionManager = null;
    private Registry<ConnectionSocketFactory> registry = null;
    private int maxConnectionCount = 300;
    private CloseableHttpClient httpClient = null;
    private ConnectionKeepAliveStrategy connectionKeepAliveStrategy = null;
    private HttpRequestRetryHandler httpRequestRetryHandler = null;
    private RequestConfig requestConfig = null;
    private String hostName = null;
    private boolean bInit = false;

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public HttpPoolManager(String hostUrl, int hostPort){

        Init(hostUrl,hostPort);
    }

    private void Init(String hostUrl, int hostPort){

        if(bInit)
            return;

        try{

            URL urlInfo = new URL(hostUrl);

            hostName = urlInfo.getHost();

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
            poolConnectionManager.setValidateAfterInactivity(1000);//池中闲置1秒就check一次该连接的有效性

            httpRequestRetryHandler = new DefaultHttpRequestRetryHandler(1,true);

            connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {
                public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {

                    // Honor 'keep-alive' header
                    HeaderElementIterator it = new BasicHeaderElementIterator(
                            httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE));
                    while (it.hasNext()) {
                        HeaderElement he = it.nextElement();
                        String param = he.getName();
                        String value = he.getValue();
                        if (value != null && param.equalsIgnoreCase("timeout")) {
                            try {
                                logger.info("host:"+hostName+" set keepAlive timeout;"+value+"s");
                                return Long.parseLong(value) * 1000;
                            } catch(NumberFormatException ignore) {
                            }
                        }
                    }
                    HttpHost target = (HttpHost) httpContext.getAttribute(
                            HttpClientContext.HTTP_TARGET_HOST);
                    if (hostName.equalsIgnoreCase(target.getHostName())) {
                        // Keep alive for 10 seconds only
                        logger.info("host:"+hostName+" dont set timeout ,so we set keepAlive timeout;10s");
                        return 10 * 1000;
                    } else {
                        // otherwise keep alive for 30 seconds
                        logger.info("other-host:"+hostName+" dont set timeout ,so we set keepAlive timeout;30s");
                        return 30 * 1000;
                    }
                }
            };

            httpClient = HttpClients.custom().setConnectionManager(poolConnectionManager)
                    .setRetryHandler(httpRequestRetryHandler)
                    .setKeepAliveStrategy(connectionKeepAliveStrategy)
                    .setConnectionReuseStrategy(new DefaultClientConnectionReuseStrategy())
                    .build();


            logger.info("init poolConnectionManager successfully");

        }catch(Exception initError){

            logger.error("init url:"+hostUrl+"[port:"+hostPort+"] occur error:"+initError);
        }

        bInit = true;
    }
}
