import com.qixunpay.Enum.PayRoute;
import org.junit.Test;

/**
 * Created by saosinwork on 2017/10/25.
 */
public class TestPayRoute {

    @Test
    public void testPayInstance(){

        try{

            PayRoute.PAYINSTANCE.Pay();

        }catch(Exception e){

            System.out.println(e);
        }


    }
}
