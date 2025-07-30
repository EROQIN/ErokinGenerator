package com.erokin.generator.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import com.erokin.generator.config.CosClientConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cos 对象存储操作
 *
 * @author <a href="https://github.com/EROQIN">Erokin</a>
 * 
 */
@Component
public class CosManager {

    public static final String COS_HOST = "https://erokin-generator-123456789.cos.ap-shanghai.myqcloud.com";

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    /**
     * 上传对象
     *
     * @param key           唯一键
     * @param localFilePath 本地文件路径
     * @return
     */
    public PutObjectResult putObject(String key, String localFilePath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, new File(localFilePath));
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 上传对象
     *
     * @param key  唯一键
     * @param file 文件
     * @return
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 下载对象
     *
     * @param key      唯一键
     * @param response
     * @throws IOException
     */
    public void download(String key, HttpServletResponse response) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
        COSObjectInputStream cosObjectInput = cosClient.getObject(getObjectRequest).getObjectContent();
        byte[] bytes = new byte[1024];
        int len;
        while ((len = cosObjectInput.read(bytes)) != -1) {
            response.getOutputStream().write(bytes, 0, len);
        }
        response.getOutputStream().flush();
        cosObjectInput.close();
    }

    /**
     * 下载对象到本地文件
     *
     * @param key           唯一键
     * @param localFilePath 本地文件路径
     * @return
     */
    public GetObjectResult download(String key, String localFilePath) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
        return cosClient.getObject(getObjectRequest, new File(localFilePath));
    }

    /**
     * 删除对象
     *
     * @param key 唯一键
     * @throws CosClientException
     * @throws CosServiceException
     */
    public void deleteObject(String key) {
        cosClient.deleteObject(cosClientConfig.getBucket(), key);
    }

    /**
     * 批量删除对象
     *
     * @param keyList
     * @return
     * @throws CosClientException
     * @throws CosServiceException
     */
    public DeleteObjectsResult deleteObjects(List<String> keyList) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(cosClientConfig.getBucket());
        ArrayList<DeleteObjectsRequest.KeyVersion> keyVersions = new ArrayList<>();
        for (String key : keyList) {
            keyVersions.add(new DeleteObjectsRequest.KeyVersion(key));
        }
        deleteObjectsRequest.setKeys(keyVersions);
        return cosClient.deleteObjects(deleteObjectsRequest);
    }

    /**
     * 删除目录
     *
     * @param delPrefix
     */
    public void deleteDir(String delPrefix) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(cosClientConfig.getBucket());
        listObjectsRequest.setPrefix(delPrefix);
        listObjectsRequest.setMaxKeys(1000);

        ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);

        ArrayList<DeleteObjectsRequest.KeyVersion> keyVersions = new ArrayList<>();
        for (COSObjectSummary cosObjectSummary : objectListing.getObjectSummaries()) {
            keyVersions.add(new DeleteObjectsRequest.KeyVersion(cosObjectSummary.getKey()));
        }

        if (keyVersions.isEmpty()) {
            return;
        }

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(cosClientConfig.getBucket());
        deleteObjectsRequest.setKeys(keyVersions);
        cosClient.deleteObjects(deleteObjectsRequest);
    }
}

