package life.hjh.community.controller;

import life.hjh.community.dto.AccessTokenDTO;
import life.hjh.community.dto.GithubUser;
import life.hjh.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description: community
 * Created by Alves on 2020/2/12 12:35
 * 定义变量到外面： ctrl atl V
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;  /*使用实例化对象*/

    @Value("${github.client.id}")/* @Values该注解会去*/
    private  String clientId;
    @Value("${gitbub.Client.secret}")
    private  String secret;
    @Value("${github.Redirect.uri}")
    private  String redirecturi;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name = "state") String state) {
        final AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirecturi);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return  "index";
    }
}
