package com.hjs.util;

import com.hjs.entity.FastDFSFile;
import org.apache.commons.io.FileUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FastDFSClient {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

    /**
     * 初始化加载FastDFS的TrackerServer配置
     */
    static {
        try {
//            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClassPathResource classPathResource = new ClassPathResource("/fdfs_client.conf");
            InputStream inputStream = classPathResource.getInputStream();
            File file = File.createTempFile("fdfs_client", ".conf");
            FileUtils.copyInputStreamToFile(inputStream,file);
            ClientGlobal.init(file.getAbsolutePath());
        } catch (Exception e) {
            logger.error("FastDFS Client Init Fail!",e);
        }
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    public static String[] upload(FastDFSFile file){
        //获取文件作者
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("author",file.getAuthor());
        //接收返回数据
        String[] uploadResult = null;
        StorageClient storageClient = null;
        try {
            //创建StorageClient客户端对象
            storageClient = getTrackerClient();

            /**
             * file.getContent():文件字节数组
             * file.getExt():文件扩展名
             * meta_list:文件作者
             */
            uploadResult = storageClient.upload_file(file.getContent(),file.getExt(),meta_list);
        } catch (Exception e) {
            logger.error("Exception when uploadind the file:"+file.getName(),e);
        }
        if (uploadResult == null && storageClient != null){
            logger.error("upload file fail,error code:"+storageClient.getErrorCode());
        }
        //获取组名
        String groupName = uploadResult[0];
        //获取文件存储路径
        String remoteFileName = uploadResult[1];
        return uploadResult;
    }

    /**
     * 获取文件信息
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static FileInfo getFile(String groupName,String remoteFileName){
        try {
            StorageClient storageClient = getTrackerClient();
//            return storageClient.get_file_info(groupName,remoteFileName);
            return storageClient.query_file_info(groupName,remoteFileName);
        } catch (Exception e) {
            logger.error("Exception: Get File from Fast DFS failed",e);
        }
        return null;
    }

    /**
     * 文件下载
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream downFile(String groupName,String remoteFileName){
        try {
            //创建StorageClient
            StorageClient storageClient = getTrackerClient();
            //下载文件
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            InputStream ins = new ByteArrayInputStream(fileByte);
            return ins;
        } catch (Exception e) {
            logger.error("Exception: Get File from Fast DFS failed",e);
        }
        return null;
    }

    /**
     * 文件删除
     * @param groupName
     * @param remoteFileName
     * @throws Exception
     */
    public static Integer deleteFile(String groupName,String remoteFileName) throws Exception{
        //创建StorageClient
        StorageClient storageClient = getTrackerClient();
        //删除文件
//        System.out.println("i = " + i);
        return storageClient.delete_file(groupName,remoteFileName);
    }



    /**
     * 获取Storage组
     * @param groupName
     * @return
     * @throws IOException
     */
    public static StorageServer[] getStoreStorages(String groupName) throws IOException{
        //创建TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        //获取TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取Storage组
        return trackerClient.getStoreStorages(trackerServer,groupName);
    }

    /**
     * 获取Storage信息，IP和端口
     * @param groupName
     * @param remoteFileName
     * @return
     * @throws IOException
     */
    public static ServerInfo[] getFetchStorages(String groupName,String remoteFileName) throws IOException{
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getFetchStorages(trackerServer,groupName,remoteFileName);
    }

    public static String getTrackerUrl() throws IOException {
        return "http://"+getTrackerServer().getInetSocketAddress().getHostString()+":"+ClientGlobal.getG_tracker_http_port()+"/";
    }

    /**
     * 获取Storage客户端
     * @return
     * @throws IOException
     */
    private static StorageClient getTrackerClient() throws IOException{
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer,null);
        return storageClient;
    }

    /**
     * 获取Tracker
     * @return
     * @throws IOException
     */
    private static TrackerServer getTrackerServer() throws IOException{
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }
}
