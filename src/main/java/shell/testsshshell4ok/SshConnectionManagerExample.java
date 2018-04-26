package shell.testsshshell4ok;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SshConnectionManagerExample {

private static Session session;
private static ChannelShell channel;
private static String username = "docker";
private static String password = "tcuser";
private static String hostname = "192.168.99.100";


private static Session getSession(){
    if(session == null || !session.isConnected()){
        session = connect(hostname,username,password);
    }
    return session;
}

private static Channel getChannel(){
    if(channel == null || !channel.isConnected()){
        try{
            channel = (ChannelShell)getSession().openChannel("shell");
            channel.connect();

        }catch(Exception e){
            System.out.println("Error while opening channel: "+ e);
        }
    }
    return channel;
}

private static Session connect(String hostname, String username, String password){

    JSch jSch = new JSch();

    try {

        session = jSch.getSession(username, hostname, 22);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setPassword(password);

        System.out.println("Connecting SSH to " + hostname + " - Please wait for few seconds... ");
        session.connect();
        System.out.println("Connected!");
    }catch(Exception e){
        System.out.println("An error occurred while connecting to "+hostname+": "+e);
    }

    return session;

}

private static void executeCommands(List<String> commands){

    try{
        Channel channel=getChannel();

        System.out.println("开始：Sending commands...");
        sendCommands(channel, commands);

        readChannelOutput(channel);
        System.out.println("结束 sending commands!");

    }catch(Exception e){
        System.out.println("错误：An error ocurred during executeCommands: "+e);
    }
}

private static void sendCommands(Channel channel, List<String> commands){

    try{
        PrintStream out = new PrintStream(channel.getOutputStream());

        out.println("#!/bin/bash");
        out.flush();

        for(String command : commands){
            out.println(command);
        }
        out.println("exit");
        out.flush();

    }catch(Exception e){
        System.out.println("Error while sending commands: "+ e);
    }

}

private static void readChannelOutput(Channel channel){

    byte[] buffer = new byte[1024];

    try{
        InputStream in = channel.getInputStream();
        String line = "";
        while (true){
            System.out.println("while true ");
            while (in.available() > 0) {
                int i = in.read(buffer, 0, 1024);
                System.out.println("i = " + i);
                if (i < 0) {
                    break;
                }
                line = new String(buffer, 0, i);
                System.out.println("读行：" + line);
            }

            /** 如果发现有字符串 mylogout, 就退出*/
            if(line.contains("mylogout")){
                break;
            }

            if (channel.isClosed()){
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee){}
        }
    }catch(Exception e){
        System.out.println("Error while reading channel output: "+ e);
    }

}

public static void close(){
    channel.disconnect();
    session.disconnect();
    System.out.println("关闭 channel 和 session");
}


public static void main(String[] args){
    List<String> commands = new ArrayList<String>();
//    commands.add("ls "); //执行时间太长打印不出来
    commands.add("docker images");
    commands.add("docker run -it ubuntu-emacs-man /bin/bash");
    commands.add("ls");         //执行时间太长打印不出来
    commands.add("echo \"mylogout\"");         //执行时间太长打印不出来
    executeCommands(commands);

    close();
}
}