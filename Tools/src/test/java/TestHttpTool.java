import com.qixunpay.Tools.HttpTool.HttpPoolManager;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by saosinwork on 2017/9/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestHttpTool {

    @Autowired
    HttpPoolManager httpPoolManager ;

    @Test
    public void testHttpPool(){

        final Logger logger = LoggerFactory.getLogger("testHttpPool");
        final Logger errLogger = LoggerFactory.getLogger("errorLogger");

        List<FutureTask<String>> list = new ArrayList<FutureTask<String>>();

        try{

            while(true){

                list.clear();
                for(int i=0;i<1000;i++){


                    final FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
                        public String call() throws Exception {



                            logger.info(Thread.currentThread().getId()+" start http request");
                            HttpPost httpPost = new HttpPost("http://10.9.28.109:9040/wftpayNotify.do");

                            long starttime = System.currentTimeMillis();
                            long begintime = starttime;
                            HttpClient client = httpPoolManager.getHttpClient();

                            logger.info("pick connection ["+Thread.currentThread().getId()+"] usetime:"+(System.currentTimeMillis()-starttime));
                            starttime = System.currentTimeMillis();

                            String data = "{\"tradeno\":\"W170911\",\"thid\":"+Thread.currentThread().getId()+"}";
                            StringEntity params = new StringEntity(data,"UTF-8");
                            params.setContentType("application/json");
                            httpPost.setEntity(params);
                            CloseableHttpResponse response =  (CloseableHttpResponse) client.execute(httpPost);

                            logger.info("httpclient execute ["+Thread.currentThread().getId()+"] usetime:"+(System.currentTimeMillis()-starttime));
                            starttime=System.currentTimeMillis();
                            int responseCode = response.getStatusLine().getStatusCode();

                            StringBuilder sb = new StringBuilder();
                            sb.append("thid:"+Thread.currentThread().getId()+":clienthashcode:"+client.hashCode()+":");
                            if(responseCode==200){

                                String output;
                                BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                                while((output=br.readLine())!=null){

                                    sb.append(output);
                                }
                                sb.append(" maybe empty");

                               logger.info("thid:"+Thread.currentThread().getId()+" ok!"+sb.toString());
                            }else{

                                sb.append(" err");
                                errLogger.error("thid:"+Thread.currentThread().getId()+" responsecode:"+responseCode);
                            }

                            if(response!=null)
                                response.close();
                            logger.info("proc result ["+Thread.currentThread().getId()+"] usetime:"+(System.currentTimeMillis()-starttime));

                            logger.info("total http["+Thread.currentThread().getId()+"] usetime:"+(System.currentTimeMillis()-begintime));
                            return sb.toString();
                        }
                    });

                    list.add(future);
                    new Thread(future).start();
                }


                for (FutureTask<String> futrue:list
                     ) {
                   try{

                       logger.info("final return data:"+futrue.get());
                   }catch(Exception ex){
                       errLogger.error(ex.getMessage());
                   }

                }

                Thread.sleep(10000);

            }


        }catch (Exception outEx){

            errLogger.error("fucking break forever:"+outEx);
        }


    }


}
