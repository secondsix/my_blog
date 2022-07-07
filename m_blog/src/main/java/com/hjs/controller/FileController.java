package com.hjs.controller;

import com.hjs.common.Result;
import com.hjs.entity.FastDFSFile;
import com.hjs.util.FastDFSClient;
import com.hjs.util.FastDFSUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class FileController {
    /**
     * 上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam(value = "file")MultipartFile file){
        String path = "";
        try {
            path=saveFile(file);
            System.out.println("path = " + path);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("e = " + e);
        }
//1.jpg        http://116.62.14.32:8080/group1/M00/00/00/rBow6mKPKGiAbV1UAAHSeFYqJKA650.jpg
//background.jpg        http://116.62.14.32:8080/group1/M00/00/00/rBow6mKPKIuAbAwgAFOXOiR-Pus598.jpg
//bg.jpg        http://116.62.14.32:8080/group1/M00/00/00/rBow6mKPKKWABmkwAAQDl562aMA555.jpg
        //文件上传地址 data/fast_data/data/00/00
        return Result.success(path);
    }

    public String saveFile(MultipartFile multipartFile) throws IOException{
        //获取文件名
        String fileName = multipartFile.getOriginalFilename();
        //获取文件内容
        byte[] content = multipartFile.getBytes();
        //获取扩展名
        String ext = "";
        if (fileName != null && !"".equals(fileName)){
            ext = fileName.substring(fileName.lastIndexOf(".")+1);
        }
        //创建文件实体类对象
        FastDFSFile fastDFSFile = new FastDFSFile(fileName,content,ext);
        //上传
        String[] uploadResults = FastDFSClient.upload(fastDFSFile);
        //拼接上传后的文件完整路径和名字，uploadResults[0]为组名，uploadResults[1]为文件名称和路径
        String path = FastDFSClient.getTrackerUrl() + uploadResults[0] + "/" + uploadResults[1];
        //返回
        return path;
    }

    @PostMapping("/deleteFile")
    public Result deleteFile(String imgName) throws Exception {
        Integer group1 = FastDFSClient.deleteFile("group1", "M00/00/00/" + imgName);
        if (group1 == 0){
            return Result.success("success");
        }else {
            return Result.fail("fail");
        }
    }

    @PostMapping("/databaesDeleteFile")
    public Result databaesDeleteFile(String fileName) throws Exception {
        String substr=fileName.substring(fileName.lastIndexOf("/")+1);
        Integer group1 = FastDFSClient.deleteFile("group1", "M00/00/00/" + substr);
//        Integer group1 = 0;
        if (group1 == 0){
            return Result.success("success");
        }else {
            return Result.fail("fail");
        }
    }

}
