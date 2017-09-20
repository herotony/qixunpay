import com.qixunpay.Tools.hessianlite.Collection.IntMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by saosinwork on 2017/9/20.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestCollection {

    @Test
    public void testInitMap(){

        int testMask = 3677 % 1000;

        IntMap map = new IntMap();
        map.put("some.f",0);
        map.put("some.t",0);
        int result = map.get("some.k");
        if(result == IntMap.NULL)
            System.out.println("shit");
        result = map.get("some.t");
    }

}
