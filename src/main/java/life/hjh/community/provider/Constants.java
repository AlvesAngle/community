package life.hjh.community.provider;


import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.auth.*;
import cn.ucloud.ufile.http.HttpClient;
import cn.ucloud.ufile.util.JLog;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Description: community
 * Created by Alves on 2020/3/24 15:27
 */
public class Constants {
    static {
        /**
         * 开启Debug级别日志
         */
        JLog.SHOW_TEST = true;
        JLog.SHOW_DEBUG = true;

        /**
         * 配置UfileClient，必须在使用UfileClient之前调用
         */
        UfileClient.configure(new UfileClient.Config(
                new HttpClient.Config(10, 5, TimeUnit.MINUTES)
                        .setTimeout(10 * 1000, 30 * 1000, 30 * 1000)
                        .setExecutorService(Executors.newSingleThreadExecutor())));
    }
    @Value("${ucloud.ufile.public-key}")/* @Values该注解会去*/
    private static String publicKey;

    @Value("${ucloud.ufile.private-key}")/* @Values该注解会去*/
    private static String privateKey;
    /**
     * 本地Bucket相关API的签名器（账号在ucloud 的API 公私钥，不能使用token）
     * 如果只用到了文件操作，不需要配置下面的bucket 操作公私钥
     */
    public static final BucketAuthorization BUCKET_AUTHORIZER = new UfileBucketLocalAuthorization(
            publicKey,
            privateKey);

    /**
     * 本地Object相关API的签名器
     * 请修改下面的公私钥
     */
    public static final ObjectAuthorization OBJECT_AUTHORIZER = new UfileObjectLocalAuthorization(
            publicKey,
            privateKey);

    /**
     * 远程Object相关API的签名器
     */
//    public static final ObjectAuthorization OBJECT_AUTHORIZER = new UfileObjectRemoteAuthorization(
//            您的公钥,
//            new ObjectRemoteAuthorization.ApiConfig(
//                    "http://your_domain/applyAuth",
//                    "http://your_domain/applyPrivateUrlAuth"
//            ));
}