import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author liango
 * @version 1.0
 * @since 2017-12-25 1:10
 */

class First {

    @Resource
    Second second ;

    public First(){
        second = new Second();
    }

    public String doSecond(){
        return second.doSecond();
    }
}
class Second {

 
    public String doSecond(){
        return "Do Something";
    }
}
@RunWith(MockitoJUnitRunner.class)
public class Test1 {
    @Mock
    Second second;

    @InjectMocks
    First first = new First();



    @Test
    public void testFirst(){
        when(second.doSecond()).thenReturn("Stubbed Second");
        assertEquals("Stubbed Second", first.doSecond());
    }
}


