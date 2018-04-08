package com.mmall.service.impl;


import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("fileService")
public class FileServiecImpl implements IFileService{
    //文件上传需要频繁调用
    public static Logger logger= LoggerFactory.getLogger(FileServiecImpl.class);
    //要上传的文件和路径,首先将文件传到webapp下的文件夹内，然后发布到ftp服务器上
    public String upload(MultipartFile file,String path){
        String filename=file.getOriginalFilename();
        //新生成文件名,用UUID来唯一标识
        String extensionName=filename.substring(filename.lastIndexOf('.'));
        String uploadFileName= UUID.randomUUID().toString()+extensionName;
        File fileDir=new File(path);
        //文件夹不存在需要创建
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        File targetFile=new File(path,uploadFileName);
        try {
            file.transferTo(targetFile);
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            e.printStackTrace();
        }finally {
            return targetFile.getName();
        }
    }
}
