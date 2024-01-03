package com.example.demo.utils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jcraft.jsch.*;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * JSch工具类
 * 交给spring管理：每个使用的地方都是单例，都是单独的这个类。（new 也可以）
 *
 * 所有方法都没有关闭（连接、会话），需要使用方自己关闭
 *
 * @author CC
 * @since 2023/11/8
 */
@Data
@Component
public class JSchUtil {
    //缓存session会话
    private Session session;

    //通道：执行命令
    private ChannelExec channelExec;
    //通道：SFTP
    private ChannelSftp channelSftp;
    //通道：执行复杂Shell命令
    private ChannelShell channelShell;


    //登陆Linux服务器
    public void loginLinux(String username, String password, String host, Integer port) {
        try {
            //每次都会重新初始化session
            if (Objects.isNull(session) || !session.isConnected()) {
                JSch jsch = new JSch();
                session = jsch.getSession(username, host, port);
                session.setPassword(password);

                // 配置Session参数
                Properties config = new Properties();
                // 不进行公钥的检查
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                // 设置连接超时时间（s/秒）
                session.setTimeout(300);
            }
            if (!session.isConnected()) {
                // 连接到远程服务器
                session.connect();
            }
        }catch(Exception e){
            throw new RuntimeException("连接Linux失败：" + e.getMessage());
        }
    }
    //获取Linux服务器字符集
    public String getLinuxEncoding(){
        String encoding = "UTF-8";  // 默认编码为UTF-8
        try{
            Process process = Runtime.getRuntime().exec("locale charmap");
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            encoding= reader.readLine(); // 读取命令输出的编码信息
        }catch (Exception e){
            e.printStackTrace();
        }

        return encoding.trim();  // 返回去除空白字符后的编码信息
    }
    public static String convertToUTF8(String input, String sourceEncoding) {
        try {
            byte[] bytes = input.getBytes(sourceEncoding);  // 使用指定编码将字符串转换为字节数组
            return new String(bytes, "UTF-8");  // 将字节数组按UTF-8编码转换为字符串
        } catch (Exception e) {
            e.printStackTrace();
            return input;  // 转换失败时返回原始字符串
        }
    }

    //执行命令：可以多次执行，然后必须调用关闭接口
    public String executeCommand(String command) {
        StringBuilder result = new StringBuilder();
        BufferedReader buf = null;
        try {
            //每次执行都创建新的通道
            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);

            //正确的流中没有数据就走错误流中去拿。
            InputStream in = channelExec.getInputStream();
            InputStream errStream = channelExec.getErrStream();
            channelExec.connect();

            buf = new BufferedReader(new InputStreamReader(in));
            String msg;
            while ((msg = buf.readLine()) != null) {
                result.append(msg);
            }

            if (StringUtils.isBlank(result.toString())) {
                buf = new BufferedReader(new InputStreamReader(errStream));
                String msgErr;
                while ((msgErr = buf.readLine()) != null) {
                    result.append(msgErr);
                }
            }
        }catch(Exception e){
            throw new RuntimeException("关闭连接失败（执行命令）：" + e.getMessage());
        }finally {
            if (Objects.nonNull(buf)) {
                try {
                    buf.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    /**
     * 执行复杂shell命令
     *
     * @param cmds 多条命令
     * @return 执行结果
     * @throws Exception 连接异常
     */
    public String execCmdByShell(List cmds) {

        StringBuilder result = new StringBuilder();
        boolean lastLoginFound = false;
        try {
            channelShell = (ChannelShell) session.openChannel("shell");

            InputStream inputStream = channelShell.getInputStream();
            channelShell.setPty(true);
            channelShell.connect();
            OutputStream outputStream = channelShell.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            for (Object cmd : cmds) {
                printWriter.println(cmd);

            }
            printWriter.flush();
            byte[] tmp = new byte[1024];
            byte[] outTmp = new byte[1024];
            boolean commandExecuted = false; // 标记是否已执行命令
            while (true) {
                while (inputStream.available() > 0) {
                    int i = inputStream.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    String s = new String(tmp, 0, i).trim();
                    System.out.println("s___"+s);
                    if (s.isEmpty() ) {  // 判断字符串是否为空或只包含空格
                        break;  // 跳出内部循环
                    }
                    if (s.contains("--More--")) {
                        outputStream.write((" ").getBytes());
                        outputStream.flush();
                    }
                    result.append(s).append("\n"); // 将读取的内容追加到结果中并添加换行符
                    System.out.println("ss::::"+result);
                    commandExecuted = true; // 设置命令已执行标志为 true


                }

                if (commandExecuted && !channelShell.isClosed()) {
                    // 如果命令已执行且通道未关闭，则跳出循环
                    break;
                }
                /*
                if (inputStream.available() <= 0) {
                    break;  // 如果输入流为空，则跳出外部循环
                }
                */
                //间隔1s后再执行
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            outputStream.close();
            inputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return result.toString();
    }

    //下载除了云服务器的文件（你自己的服务器）：因为云服务器，像阿里云服务器下载文件好像是一段一段给你的，不是一起给你。
    public void downloadOtherFile(String remoteFileAbsolutePath, String fileName, HttpServletResponse response) {
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            //获取输入流
            InputStream inputStream = channelSftp.get(remoteFileAbsolutePath);
            //直接下载到本地文件
//            channelSftp.get(remoteFileAbsolutePath, "D:\\Develop\\Test\\studio-3t-x64.zip");

            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/octet-stream;charset=".concat(StandardCharsets.UTF_8.name()));
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=".concat(
                            URLEncoder.encode(fileName, StandardCharsets.UTF_8.name())
                    ));
            ServletOutputStream out = response.getOutputStream();

            // 从InputStream输入流读取数据 并写入到ServletOutputStream输出流
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            out.close();
        }catch(Exception e){
            throw new RuntimeException("关闭连接失败（下载文件）：" + e.getMessage());
        }
    }

    //下载云服务器的文件（因为云服务器传文件是一段一段的，所以不能直接像操作我们的服务器一样直接下载）（阿里云为例）
    public void downloadCloudServerFile(String remoteFileAbsolutePath, String fileName, HttpServletResponse response) {
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            //获取输入流
            InputStream inputStream = channelSftp.get(remoteFileAbsolutePath);

            //阿里云应该是断点续传，后面研究……

        }catch(Exception e){
            throw new RuntimeException("关闭连接失败（下载文件）：" + e.getMessage());
        }
    }

    //ls命令：获取文件夹的信息
    public String ls(String path){
        StringBuilder sb = new StringBuilder();
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            Vector ls = channelSftp.ls(path);
            Iterator iterator = ls.iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                System.out.println(next);
                sb.append(next);
            }
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return sb.toString();
    }

    //关闭通道：释放资源
    private void closeChannel(){
        //不为空，且已经连接：关闭
        if (Objects.nonNull(channelExec)) {
            channelExec.disconnect();
        }
        if (Objects.nonNull(channelSftp)) {
            channelSftp.disconnect();
        }
        if (Objects.nonNull(channelShell)) {
            channelShell.disconnect();
        }
    }

    /** 关闭通道、关闭会话：释放资源
     * spring销毁前，关闭 所有会话 及 所有通道
     */
    @PreDestroy
    public void closeAll(){
        System.out.println("连接关闭");

        this.closeChannel();

        if (Objects.nonNull(session) && session.isConnected()) {
            session.disconnect();
        }
    }
}
