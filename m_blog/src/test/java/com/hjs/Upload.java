package com.hjs;

import com.hjs.entity.FastDFSFile;
import com.hjs.util.FastDFSClient;
import com.hjs.util.FastDFSUtils;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
public class Upload {

    @Test
    public void test01(){
        File file = new File("C:\\Users\\hjs\\Desktop\\bg.jpg");

        try {
            String fdfsPath = FastDFSUtils.upload(file);
            System.out.println("文件上传成功，文件ID： " + fdfsPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test02(){
        try {
            long downloadSize = FastDFSUtils.download(new FileOutputStream(new File("C:\\Users\\hjs\\Desktop\\3.jpg")), "gourp1/M00/00/00/rBow6mKMg0SEOjZdAAAAAFYqJKA113.jpg");
            System.out.println("文件下载完成，文件大小（单位 byte）： " + downloadSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test03(){
        try {
            FastDFSUtils.delete("gourp1/M00/00/00/rBow6mKNyLuAIROYAADTJIHxjCI561.jpg");
//            FastDFSUtils.delete("gourp1/M00/00/00/rBow6mKNyc6AFDS2AADTJIHxjCI323.jpg-m");
            boolean exists = FastDFSUtils.exists("gourp1/M00/00/00/rBow6mKNyLuAIROYAADTJIHxjCI561.jpg");
            System.out.println("文件是否还存在： " + exists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test04(){
        try {

//            boolean group1 = FastDFSUtils.exists("group1/M00/00/00/rBow6mKO5paAZBpTAAHSeFYqJKA228.jpg");

            FileInfo group1 = FastDFSClient.getFile("group1", "M00/00/00/rBow6mKO7w-ABhS8AAHSeFYqJKA043.jpg");

            System.out.println("group1 = " + group1);
//            System.out.println("文件是否还存在： " + exists);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String saveFile(MultipartFile multipartFile) throws IOException {
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

    @Test
    public void deleteFile() throws Exception {

//        FastDFSClient.deleteFile("group1","M00/00/00/rBow6mKO5paAZBpTAAHSeFYqJKA228.jpg");


//        FastDFSClient.getFile("gorup1/M00/00/00/","rBow6mKN3KWAHmsnAADTJIHxjCI724.jpg");
//        FastDFSClient.deleteFile("gorup1","M00/00/00/rBow6mKMhf2EMZeRAAAAAFYqJKA714.jpg");

        Integer group1 = FastDFSClient.deleteFile("group1", "M00/00/00/rBow6mLFN5CAV_j_AAHlhRzcF60017.png");
        if (group1 == 0){
            System.out.println("删除成功！");
        }
    }
}
