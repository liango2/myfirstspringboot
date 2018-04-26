package shell.testsshshell;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:CommonUtils.xml")
public class SSHClientTest {

    @Autowired
    private SSHClient sshClient;

    @Before
    public void setUp() throws Exception {
        sshClient.setHost("localhost").setPort(10022).setUsername("root").setPassword("root");
        sshClient.login();
    }

    @Test
    public void sendCmd() throws Exception {
        String ret = sshClient.sendCmd("pwd");

        System.out.println("******************************");
        System.out.println(ret);
        System.out.println("******************************");

        Assert.assertNotNull(ret);
        Assert.assertTrue(ret.length() > 0);

        ret = sshClient.sendCmd("vmstat");

        System.out.println("******************************");
        System.out.println(ret);
        System.out.println("******************************");

        Assert.assertNotNull(ret);
        Assert.assertTrue(ret.length() > 0);
    }

    @After
    public void tearDown() throws Exception {
        sshClient.logout();
    }
}

