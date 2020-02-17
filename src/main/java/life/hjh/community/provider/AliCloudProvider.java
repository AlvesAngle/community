package life.hjh.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * Description: community
 * Created by Alves on 2020/3/10 18:28
 */
@Service
public class AliCloudProvider {
    public static String endpoint = "http://oss-cn-beijing.aliyuncs.com";//region
    // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
    public static String accessKeyId = "LTAI4Fv6powyYSrHMVrRk8zw";
    public static String accessKeySecret = "vi5pa87gk82bBsz1LwkLpZqKlDjJF4";
    public static String bucketName = "self-leaning-community"; //bucketName
    public static String key = "community-Images/"; //相当于需要上传的图片的目录
    public static String urlPrefix = "";

    public String uploadfile(InputStream fileStream, String mimeType,String fileName){

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传文件流。
        PutObjectResult putObjectResult = ossClient.putObject(bucketName, key, fileStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        String url = ossClient.generatePresignedUrl(bucketName, fileName, expiration).toString();
        System.out.println(url);
        return null;
    }

}
