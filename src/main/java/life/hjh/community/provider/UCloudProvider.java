package life.hjh.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.ObjectAuthorizer;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.DownloadFileBean;
import cn.ucloud.ufile.bean.ObjectProfile;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import cn.ucloud.ufile.util.JLog;
import life.hjh.community.dto.SaveFileDTO;
import life.hjh.community.exception.CustomizeErrorCode;
import life.hjh.community.exception.CustomizeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

/**
 * Description: community
 * Created by Alves on 2020/3/10 11:03
 * 用于Ucloud 对象存储 文件上传下载
 */
@Service
public class UCloudProvider {
    @Value("${ucloud.ufile.public-key}")/* @Values该注解会去*/
    private  String publicKey;

    @Value("${ucloud.ufile.private-key}")/* @Values该注解会去*/
    private  String privateKey;

    @Value("${ucloud.ufile.region}")
    private String region; //地区名

    @Value("${ucloud.ufile.suffix}")
    private String suffix; //域名（）后缀

    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName ; // 空间目录名

    @Value("${ucloud.ufile.expires-duration}") //十年24*60*60 *365*10
    private Integer expiresDuration ; //有效时限 (当前时间开始计算的一个有效时间段, 单位：Unix time second。 eg: 24*60*60 = 1天有效)
    //

    //下载文件的地址
    private String saveAsPath;

    //SpringBoot 可以自动获取来着前端传过来的 文件流
    public String upload(InputStream fileStream, String mimeType,String fileName){
        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length>1){
            generatedFileName =  UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        } else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        //PutStreamApi putObject(InputStream inputStream, String mimeType) 通过Ctrl 查找文件流的上传方法
        try {
            // 对象相关API的授权器 bucket
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);
            // 对象操作需要ObjectConfig来配置您的地区和域名后缀
            ObjectConfig config = new ObjectConfig(region, "ufileos.com");

            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(long bytesWritten, long contentLength) {

                        }
                    })
                    .execute();
                    if (response != null && response.getRetCode() ==0 ){
                        String url = UfileClient.object(objectAuthorization, config)
                                .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName, expiresDuration)
                                .createUrl();
                        return url;
                    }else {
                        throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);

                    }
        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }
    public SaveFileDTO uploadFile(InputStream fileStream, String mimeType,String fileName){
        SaveFileDTO fileDTO = new SaveFileDTO();
        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length>1){
            generatedFileName = filePaths[filePaths.length - 2] + "-" + UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
            fileDTO.setFileName(generatedFileName);
        } else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        //PutStreamApi putObject(InputStream inputStream, String mimeType) 通过Ctrl 查找文件流的上传方法
        try {
            // 对象相关API的授权器 bucket
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);
            // 对象操作需要ObjectConfig来配置您的地区和域名后缀
            ObjectConfig config = new ObjectConfig(region, "ufileos.com");

            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(long bytesWritten, long contentLength) {

                        }
                    })
                    .execute();
            if (response != null && response.getRetCode() ==0 ){
                String url = UfileClient.object(objectAuthorization, config)
                        .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName, expiresDuration)
                        .createUrl();
                fileDTO.setUrl(url);
                //返回文件保存信息对象
                return fileDTO;
            }else {
                throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);

            }
        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }
    private String TAG = "Download file ";

    //下载文件   String keyName, String bucketName, String localDir, String saveName
    public  void downloadFile(String keyName,String localDir, String saveName) {
        // 对象相关API的授权器 bucket
        ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);
        // 对象操作需要ObjectConfig来配置您的地区和域名后缀
        ObjectConfig config = new ObjectConfig(region, "ufileos.com");


        //keyName 云端文件名
        try {
            // Constants.OBJECT_AUTHORIZER
            ObjectProfile profile = UfileClient.object(objectAuthorization, config)
                    .objectProfile(keyName, bucketName)
                    .execute();

            JLog.D(TAG+keyName, String.format("[res] = %s", (profile == null ? "null" : profile.toString())));
            if (profile == null)
                return;

            DownloadFileBean response = UfileClient.object(objectAuthorization, config)
                    .downloadFile(profile)
                    .saveAt(localDir, saveName)   //localDir 下载目录, saveName
                    /**
                     * 选择要下载的对象的范围，Default = (0, whole size)
                     */
//              .withinRange(0, 0)
                    /**
                     * 配置同时分片下载的进程数，Default = 10
                     */
//              .together(5)
                    /**
                     * 是否覆盖本地已有文件, Default = true;
                     */
                    .withCoverage(false)
                    /**
                     * 指定progress callback的间隔
                     */
//              .withProgressConfig(ProgressConfig.callbackWithPercent(10))
                    /**
                     * 配置读写流Buffer的大小, Default = 256 KB, MIN = 4 KB, MAX = 4 MB
                     */
//                    .setBufferSize(4 << 20)
                    /**
                     * 配置进度监听
                     */
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(long bytesWritten, long contentLength) {
                            JLog.D(TAG+keyName, String.format("[progress] = %d%% - [%d/%d]", (int) (bytesWritten * 1.f / contentLength * 100), bytesWritten, contentLength));
                        }
                    })
                    .execute(); //下载
            JLog.D(TAG, String.format("[res] = %s", (response == null ? "null" : response.toString())));
        } catch (UfileClientException e) {
            e.printStackTrace();
        } catch (UfileServerException e) {
            e.printStackTrace();
        }

    }
}
