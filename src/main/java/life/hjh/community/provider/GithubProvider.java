package life.hjh.community.provider;

import life.hjh.community.dto.AccessTokenDTO;
import okhttp3.*;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.*;
import  life.hjh.community.dto.GithubUser;
import java.io.IOException;

/**
 * Description: community
 * Created by Alves on 2020/2/12 14:00
 * 快捷鍵：ctrl shift n 查找文件
 * 鼠標指向 錯誤，按住 alt enter
 * 在pom.xml里面 使用alt insert 可以导入maven文件
 * 定义变量到外面： ctrl atl V
 */
@Component   /*将该类加载到SpringBoot 的 上下文   控制反转IOS*/
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
             String string = response.body().string();
            String[] split = string.split("&");
            String token = string.split("&")[0].split("=")[1];
            System.out.println(string);
             return token;
        } catch (IOException e) {

            e.printStackTrace();
        }
        return  null;
  }
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class); /*自动将string 对象自动转换成 class 对象*/
            return githubUser;
        } catch (Exception e) {
            e.printStackTrace();
            //log.error("getUser error,{}", accessToken, e);
        }
        return null;
    }
}


