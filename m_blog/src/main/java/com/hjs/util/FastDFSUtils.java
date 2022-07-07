package com.hjs.util;

import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * Fast DFS系统文件操作上传、下载客户端类
 *
 * @author pengjunlee
 * @create 2019-09-17 10:25
 */

public class FastDFSUtils {

    private static Logger logger = LoggerFactory.getLogger(FastDFSUtils.class);

    private static StorageClient1 client;

    static {
        try {
            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(filePath);
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            client = new StorageClient1(trackerServer, storageServer);
        } catch (Exception e) {
            logger.error("FastDFS init failure!");
        }
    }

    /**
     * 上传一个本地文件到分布式中
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String upload(File file) throws Exception {
        return upload(new FileInputStream(file), file.getName(), file.length());
    }

    /**
     * 上传一个流，调用后in会自动关闭
     *
     * @param in
     * @param fileName
     * @param fileSize
     * @return
     * @throws Exception
     */
    public static String upload(InputStream in, String fileName, long fileSize) throws Exception {
        try {
            String extName = fileExt(fileName);
            String ret = client.upload_appender_file1(null, fileSize, new UploadStream(in, fileSize), extName, null);
            if (ret == null) {
                throw new IOException("upload fileName:" + fileName + " error");
            }
            return ret;
        } finally {
            in.close();
        }
    }

    /**
     * 把本地文件追加到分布式文件系统中
     *
     * @param file
     * @param dfsPath
     * @return
     * @throws Exception
     */
    public static long append(File file, String dfsPath) throws Exception {
        return append(new FileInputStream(file), dfsPath, file.length());
    }

    /**
     * 追加文件，in会被关闭
     *
     * @param in
     * @param dfsPath
     * @param appendSize
     * @return
     * @throws Exception
     */
    public static long append(InputStream in, String dfsPath, long appendSize) throws Exception {
        try {
            int ret = client.append_file1(dfsPath, appendSize, new UploadStream(in, appendSize));
            if (ret != 0) {
                throw new IOException("uploadAppend dfsPath:" + dfsPath + " error");
            }
            return appendSize;
        } finally {
            in.close();
        }
    }

    /**
     * 下载文件,方法调用后，out会被强制关闭
     *
     * @param out
     * @param dfsPath
     * @return
     * @throws Exception
     */
    public static long download(OutputStream out, String dfsPath) throws Exception {
        try {
            DownloadStream downloadStream = new DownloadStream(out);
            int ret = client.download_file1(dfsPath, downloadStream);
            if (ret != 0) {
                throw new IOException("download dfsPath:" + dfsPath + " error");
            }
            return downloadStream.downloadSize;
        } finally {
            out.close();
        }
    }

    /**
     * 下载文件,方法调用后，out会被强制关闭
     *
     * @param out
     * @param dfsPath
     * @return
     * @throws Exception
     */
    public static long download(OutputStream out, String dfsPath, long offset, long bytes) throws Exception {
        try {
            DownloadStream downloadStream = new DownloadStream(out);
            int ret = client.download_file1(dfsPath, offset, bytes, downloadStream);
            if (ret != 0) {
                throw new IOException("download dfsPath:" + dfsPath + " error");
            }
            return downloadStream.downloadSize;
        } finally {
            out.close();
        }
    }

    /**
     * 删除指定位置文件
     *
     * @param dfsPath
     * @throws Exception
     */
    public static void delete(String dfsPath) throws Exception {
        int ret = client.delete_file1(dfsPath);
        if (ret != 0)
            throw new IOException("delete dfsPath:" + dfsPath + " error");
    }

    /**
     * 获取文件大小；返回负数表示文件不存在，其中-1表示正常情况文件不存在，-2表示出异常了
     *
     * @param dfsPath
     * @return
     * @throws Exception
     */
    public static long length(String dfsPath) {
        try {
            FileInfo fileInfo = client.get_file_info1(dfsPath);
            if (fileInfo == null) {
                return -1;
            }
            return fileInfo.getFileSize();
        } catch (Throwable ex) {
            return -2;
        }
    }

    /**
     * 判断Fast是否存在指定路径的文件信息
     *
     * @param dfsPath
     * @return
     * @throws Exception
     */
    public static boolean exists(String dfsPath) throws Exception {
        if (dfsPath == null || dfsPath.trim().length() == 0) {
            return false;
        }

        try {
            FileInfo fileInfo = client.get_file_info1(dfsPath);
            System.out.println("fileInfo = " + fileInfo);
            return fileInfo != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从路径中提取文件后缀名，不带句号
     *
     * @param fileName
     * @return
     */
    private static String fileExt(String fileName) {
        if (fileName == null)
            return "";

        int dot = fileName.lastIndexOf(".");
        if (dot == -1)
            return "";

        String ret = fileName.substring(dot + 1);
        for (int i = 0; i < ret.length(); i++) {
            char c = ret.charAt(i);
            boolean isAlpha = ('0' <= c && c <= '9') || ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
            if (!isAlpha) {
                return "";
            }
        }
        return ret;
    }

    private static class DownloadStream implements DownloadCallback {

        private long downloadSize = 0L;

        private OutputStream out;

        public DownloadStream(OutputStream out) {
            this.out = out;
        }

        public int recv(long fileSize, byte[] data, int bytes) {
            try {
                this.out.write(data, 0, bytes);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            this.downloadSize += bytes;
            return 0;
        }
    }
}
