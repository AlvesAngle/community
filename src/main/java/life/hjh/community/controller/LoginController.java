package life.hjh.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import life.hjh.community.dto.UserDTO;
import life.hjh.community.model.User;
import life.hjh.community.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Description: community
 * Created by Alves on 2020/4/6 15:44
 */
@Controller
public class LoginController {


    @Autowired
    private UserService userService;

    private Producer kaptchaProducer=null;

    @Autowired
    public void setCaptchaProducer(Producer kaptchaProducer) {
        this.kaptchaProducer = kaptchaProducer;
    }



    // 后台登录界面请求
    @RequestMapping("/back-stage/loginView")
    public String showWelcome(Model model){
        return "management/login";
    }

    /*
    * 验证码
    * 使用  kaptcha 插件 生成验证码
    *
    */
    @GetMapping("/getVerifyCode")
    public ModelAndView verifyCode(HttpServletRequest request, HttpServletResponse response) {
        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = kaptchaProducer.createText();
        // 将验证码 存入session
        request.getSession().setAttribute("captcha", capText);
        BufferedImage bi = kaptchaProducer.createImage(capText);
        // 使用servlet 输出流
        ServletOutputStream out = null;
        try {

            out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
    // 后台管理员登录登录
    @ResponseBody
    @RequestMapping(value = "/back-stage/login",method = RequestMethod.POST)
    public JSONObject login(@RequestBody JSONObject data,
                        HttpServletRequest request,   //自动获取请求中的 http request 上下文
                        HttpServletResponse response) {

        JSONObject info = data.getJSONObject("userInfo");
        UserDTO userInfo = JSONObject.toJavaObject(info, UserDTO.class);//将 json 转换为对象
        User user = new User();
        //生成 用户token
        String token = UUID.randomUUID().toString();
        // 获取 验证码
        String captcha = (String) request.getSession().getAttribute("captcha");
        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();


        if (data != null){
            //判断验证码是否 相等
            //忽视大小写 判断验证码是否相等
            boolean flag = StringUtils.equalsAnyIgnoreCase(captcha, userInfo.getCaptcha());
            if (flag){
                user.setAccountId(userInfo.getAccountId());
                user.setPassword(userInfo.getPassword());
                user.setToken(token);
                //更新或者 插入用户登录数据
                map = userService.loginBS(user);
                //登录成功,写cookie
                response.addCookie(new Cookie("adminToken",token));

                //清除 验证码 session
                request.getSession().removeAttribute("captcha");
            }else{
                //
                map.put("code","403");
                map.put("msg","验证码错误!");
            }


        }
        JSONObject json = new JSONObject(map);
        return  json;

    }
    // 登出
    @GetMapping("/back-stage/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){

        //清除 cookie
        Cookie cookie = new Cookie("adminToken",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        //清除 session
        request.getSession().removeAttribute("adminUser");
        return "redirect:/back-stage/loginView";
    }
    //修改个人资料界面
    @RequestMapping("/back-stage/editUserInfoView")
    public String showEditUserInfoView(Model model){
        return "management/user-setting";
    }
    //修改个人资料
    @ResponseBody
    @RequestMapping(value = "/back-stage/modifiedUserInfo",method = RequestMethod.POST)
    public JSONObject editUserInfo(@RequestBody JSONObject data,
            HttpServletRequest request  //自动获取请求中的 http request 上下文
    ){
        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        JSONObject json = new JSONObject(map);
        return  json;
    }
    //修改密码界面
    @RequestMapping("/back-stage/editPasswordView")
    public String showEditPasswordView(Model model){
        return "management/user-password";
    }
    //修改密码
    @ResponseBody
    @RequestMapping(value = "/back-stage/modifiedPassword",method = RequestMethod.POST)
    public JSONObject editPassword(@RequestBody JSONObject data,
                                   HttpServletRequest request,
                                   HttpServletResponse response
                                   ){
        JSONObject Info = data.getJSONObject("passwordInfo");
        Map<String,Object> userInfo = JSONObject.toJavaObject(Info,Map.class);
        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        String oldPassword = String.valueOf(userInfo.get("old_password"));
        String newPassword = String.valueOf(userInfo.get("new_password"));

        //获取 分享资源用户的信息 ，来自session
        User user = (User)request.getSession().getAttribute("adminUser");
        user.setPassword(newPassword);
        //执行修改密码
        boolean edit = userService.editPassword(oldPassword, user);
        if(edit){
            /*修改成功
            * */
            //清除 cookie
            Cookie cookie = new Cookie("adminToken",null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            //清除 session
            request.getSession().removeAttribute("adminUser");
            //return "redirect:/back-stage/loginView";
            map.put("code","200");
            map.put("msg","修改密码成功!");

        }else {
            map.put("code","405");
            map.put("msg","当前密码输入有误!请从新输入");
        }

        JSONObject json = new JSONObject(map);
        return  json;
    }

}
