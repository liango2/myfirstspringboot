package shell.testsshshell;

import com.jcraft.jsch.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;


/**
 * SSHClient based on jsch
 * examples: com.wow.remoteutils.SSHClientTest
 *
 */
@Component
public class SSHClient {

    /**
     * Server Host IP Address，default value is localhost
     */
    private String host = "localhost";

    /**
     * Server SSH Port，default value is 22
     */
    private Integer port = 22;

    /**
     * SSH Login Username
     */
    private String username = "";

    /**
     * SSH Login Password
     */
    private String password = "";

    /**
     * JSch
     */
    private JSch jsch = null;

    /**
     * ssh session
     */
    private Session session = null;

    /**
     * ssh channel
     */
    private Channel channel = null;

    /**
     * timeout for session connection
     */
    private final Integer SESSION_TIMEOUT = 30000;

    /**
     * timeout for channel connection
     */
    private final Integer CHANNEL_TIMEOUT = 30000;

    /**
     * the interval for acquiring ret
     */
    private final Integer CYCLE_TIME = 100;

    public SSHClient() {
        // initialize
        jsch = new JSch();
    }

    public SSHClient setHost(String host) {
        this.host = host;
        return this;
    }

    public SSHClient setPort(Integer port) {
        this.port = port;
        return this;
    }

    public SSHClient setUsername(String username) {
        this.username = username;
        return this;
    }

    public SSHClient setPassword(String password) {
        this.password = password;
        return this;
    }


    /**
     * login to server
     *
     * @param username
     * @param password
     */
    public void login(String username, String password) {

        this.username = username;
        this.password = password;

        try {
            if (null == session) {

                session = jsch.getSession(this.username, this.host, this.port);
                session.setPassword(this.password);
                session.setUserInfo(new MyUserInfo());

                // It must not be recommended, but if you want to skip host-key check,
                // invoke following,
                session.setConfig("StrictHostKeyChecking", "no");
            }

            session.connect(SESSION_TIMEOUT);
        } catch (JSchException e) {
            System.err.println(e);
            this.logout();
        }
    }

    /**
     * login to server
     */
    public void login() {
        this.login(this.username, this.password);
    }

    /**
     * logout of server
     */
    public void logout() {
        this.session.disconnect();
    }

    /**
     * send command through the ssh session,return the ret of the channel
     *
     * @return
     */
    public String sendCmd(String command) {

        String ret = "";

        // judge whether the session or channel is connected
        if (!session.isConnected()) {
            this.login();
        }

        // open channel for sending command
        try {
            this.channel = session.openChannel("exec");
            ((ChannelExec) this.channel).setCommand(command);
            this.channel.connect(CHANNEL_TIMEOUT);

            // no output stream
            channel.setInputStream(null);

            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();

            // acquire for ret
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;

                    ret = new String(tmp, 0, i);
                    System.out.print(ret);
                }

                // quit the process of waiting for ret
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }

                // wait every 100ms
                try {
                    Thread.sleep(CYCLE_TIME);
                } catch (Exception ee) {
                    System.err.println(ee);
                }
            }

        } catch (JSchException e) {
            e.printStackTrace();
            System.err.println(e);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e);
        } finally {
            // close channel
            this.channel.disconnect();
        }

        return ret;
    }

    /**
     * customized userinfo
     */
    private static class MyUserInfo implements UserInfo {
        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean promptPassword(String s) {
            return false;
        }

        @Override
        public boolean promptPassphrase(String s) {
            return false;
        }

        @Override
        public boolean promptYesNo(String s) {
            return true;
        }

        @Override
        public void showMessage(String s) {
            System.out.println("showMessage:" + s);
        }
    }
}
