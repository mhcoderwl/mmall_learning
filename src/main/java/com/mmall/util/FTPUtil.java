package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


public class FTPUtil {
    public static final Logger logger= LoggerFactory.getLogger(FTPUtil.class);
    private static final String ftpIp=PropertiesUtil.getProperty("ftp.server.ip");
    private static final int ftpPort=Integer.parseInt(PropertiesUtil.getProperty("ftp.server.port"));
    private static final String ftpUser=PropertiesUtil.getProperty("ftp.user");
    private static final String ftpPass=PropertiesUtil.getProperty("ftp.pass");
    private String ip;
    int port;
    String user;
    String passwd;
    FTPClient ftpClient;
    private FTPUtil(String ip,int port,String user,String passwd){
        this.ip=ip;
        this.passwd=passwd;
        this.port=port;
        this.user=user;
    }
    public static boolean uploadFile(List<File> files)throws IOException{
        FTPUtil ftpUtil=new FTPUtil(ftpIp,ftpPort,ftpUser,ftpPass);
        logger.info("开始连接服务器");
        boolean result=ftpUtil.uploadFile("img",files);
        logger.info("结束上传，上传结果:{}",result);
        return result;
    }
    private  boolean uploadFile(String remotePath,List<File> files) throws IOException{
        boolean upload=true;
        if(connectFTPServer(this.ip,this.port,this.user,this.passwd)){
            FileInputStream fis=null;
            try {
                ftpClient.changeWorkingDirectory(remotePath);//切换到要上传的地方
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();//打开被动模式
                for(File target:files){
                    fis=new FileInputStream(target);
                    ftpClient.storeFile(target.getName(),fis);
                }
            } catch (IOException e) {
                logger.error("上传失败",e);
                upload=false;
                e.printStackTrace();
            }finally {
                    fis.close();
                    ftpClient.disconnect();
                }
            }
        return upload;
    }
    private boolean connectFTPServer(String ip,int port,String user,String passwd){
        ftpClient=new FTPClient();
        boolean isSucess=false;
        try {
            ftpClient.connect(ip,port);
            isSucess=ftpClient.login(user,passwd);
        } catch (IOException e) {
            logger.error("连接ftp异常",e);
            e.printStackTrace();
        }finally {
            return isSucess;
        }
    }


}
