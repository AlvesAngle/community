package life.hjh.community.controller;

import life.hjh.community.dto.AccessTokenDTO;
import life.hjh.community.dto.GithubUser;
import life.hjh.community.mapper.UserMapper;
import life.hjh.community.model.User;
import life.hjh.community.provider.GithubProvider;
import life.hjh.community.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Description: community
 * Created by Alves on 2020/2/12 12:35
 * 定义变量到外面： ctrl atl V
 * idea 修改变量名快捷键： shift F6
 * idea 删除当前行的快捷键：Ctrl X ，硬核当前行 Ctrl Y
 */
// 登录controller
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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;


    /*获取GitHub信息成功后登录返回*/
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,   /*自动获取请求中的 http request 上下文*/
                           HttpServletResponse response) {
        final AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        // 从前端获取 数据
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirecturi);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //通过GitHub 返回的accessToken 查询信息
        GithubUser githubUser = githubProvider.getUser(accessToken);
        //System.out.println(user.getName());
        /*登录成功 更新数据库*/
        if (githubUser != null || githubUser.getId() !=null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));

            user.setAvatarUrl(githubUser.getAvatar_url());

            /*更新或者 插入用户登录数据*/
            userService.createOrUpdate(user);
            //登录成功,写cookie
            response.addCookie(new Cookie("token",token));

            /*            request.getSession().setAttribute("user",githubUser);*/
            return "redirect:/";

        } else {
            //登录失败，重新登录
            return "redirect:/";
        }

        //return  "index";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        //清除 session
        request.getSession().removeAttribute("user");
        //清除 cookie
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(@RequestParam(name="name") String name,
                           @RequestParam(name="emailr") String email,
                           @RequestParam(name = "passwordr") String password,
                           HttpServletRequest request,   //自动获取请求中的 http request 上下文
                           HttpServletResponse response) {
        String accoutId = email ;//注册的 邮箱就是账号
        User user = new User();
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setName(name);
        user.setAccountId(accoutId);
        user.setPassword(password);
        System.out.println(user);
        //更新或者 插入用户登录数据
        userService.loginOrRegister(user);

        //登录成功,写cookie
        response.addCookie(new Cookie("token",token));

        return "redirect:/";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam(name="email") String email,
                        @RequestParam(name = "password") String password,
                        HttpServletRequest request,   //自动获取请求中的 http request 上下文
                        HttpServletResponse response) {

        String accoutId = email ;
        User user = new User();
        String token = UUID.randomUUID().toString();

        user.setAccountId(accoutId);
        user.setPassword(password);
        user.setToken(token);


        //更新或者 插入用户登录数据
        userService.loginOrRegister(user);
        //登录成功,写cookie
        response.addCookie(new Cookie("token",token));

        return "redirect:/";
    }
    @GetMapping("/user")
    public String loginUser(){

        return "user";
    }


}
