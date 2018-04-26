import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liango
 * @version 1.0
 * @since 2017-12-28 23:35
 */
@RunWith(MockitoJUnitRunner.class)
public class TestFilterStr {
    @Test
    public void test1(){
        String mydata = "some string with 'the data i want' inside";
        Pattern pattern = Pattern.compile("'(.*?)'");
        Matcher matcher = pattern.matcher(mydata);
        if (matcher.find())
        {
//            System.out.println(matcher.group(1));
            // print the group out for verification
            System.out.format("'%s'\n", matcher.group(1));
        }


    }
}
