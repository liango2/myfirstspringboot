package shell.testsshshell2;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import expect4j.Closure;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;
import org.apache.oro.text.regex.MalformedPatternException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SSHClient_bad {

    private static final int COMMAND_EXECUTION_SUCCESS_OPCODE = -2;
    private static String ENTER_CHARACTER = "\r";
    private static final int SSH_PORT = 22;
    private List<String> lstCmds = new ArrayList<String>();
//    private static String[] linuxPromptRegEx = new String[]{"$", "\\>", "#", "~#"};
    private static String[] linuxPromptRegEx = new String[]{"docker@default:~\\$"};

    private Expect4j expect = null;
    private StringBuilder buffer = new StringBuilder();
    private String userName;
    private String password;
    private String host;

    /**
     * @param host
     * @param userName
     * @param password
     */
    public SSHClient_bad(String host, String userName, String password) {
        this.host = host;
        this.userName = userName;
        this.password = password;
    }

    /**
     * @param cmdsToExecute
     */
    public String execute(List<String> cmdsToExecute) {
        this.lstCmds = cmdsToExecute;

        Closure closure = new Closure() {
            public void run(ExpectState expectState) throws Exception {
                buffer.append(expectState.getBuffer());
            }
        };
        List<Match> lstPattern = new ArrayList<Match>();
        for (String regexElement : linuxPromptRegEx) {
            try {
                Match mat = new RegExpMatch(regexElement, closure);
                lstPattern.add(mat);
            } catch (MalformedPatternException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            expect = SSH();
            boolean isSuccess = true;
            for (String strCmd : lstCmds) {
                isSuccess = isSuccess(lstPattern, strCmd);
                if (!isSuccess) {
                    isSuccess = isSuccess(lstPattern, strCmd);
                }
            }

            checkResult(expect.expect(lstPattern));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection();
        }
        return buffer.toString();
    }

    /**
     * @param objPattern
     * @param strCommandPattern
     * @return
     */
    private boolean isSuccess(List<Match> objPattern, String strCommandPattern) {
        System.out.println("参数： objPattern = [" + objPattern + "], strCommandPattern = [" + strCommandPattern + "]");
        try {
            boolean isFailed = checkResult(expect.expect(objPattern));
            System.out.println("是否： isFailed = " + isFailed);
            if (!isFailed) {
                expect.send(strCommandPattern);
                expect.send(ENTER_CHARACTER);
                return true;
            }
            return false;
        } catch (MalformedPatternException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * @param hostname
     * @param username
     * @param password
     * @param port
     * @return
     * @throws Exception
     */
    private Expect4j SSH() throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(userName, host, SSH_PORT);
        if (password != null) {
            session.setPassword(password);
        }
        Hashtable<String, String> config = new Hashtable<String, String>();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect(60000);
        ChannelShell channel = (ChannelShell) session.openChannel("shell");
        Expect4j expect = new Expect4j(channel.getInputStream(), channel.getOutputStream());
        channel.connect();
        return expect;
    }

    /**
     * @param intRetVal
     * @return
     */
    private boolean checkResult(int intRetVal) {
        if (intRetVal == COMMAND_EXECUTION_SUCCESS_OPCODE) {
            return true;
        }
        return false;
    }

    /**
     *
     */
    private void closeConnection() {
        if (expect != null) {
            expect.close();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SSHClient_bad ssh = new SSHClient_bad("192.168.99.100", "docker", "tcuser");
        List<String> cmdsToExecute = new ArrayList<String>();
//        cmdsToExecute.add("ls");
        cmdsToExecute.add("pwd");
        cmdsToExecute.add("cd /");
        cmdsToExecute.add("pwd");

        String outputLog = ssh.execute(cmdsToExecute);
        System.out.println("outputLog = " + outputLog);
    }
}