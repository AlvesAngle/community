package life.hjh.community.interceptor;

import life.hjh.community.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description: community
 * Created by Alves on 2020/4/19 2:52
 */
@Service
public class bsMainInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //验证登录
        User user = (User)request.getSession().getAttribute("adminUser");
        System.out.println(user);
        //登录失败 返回 后台登录界面
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/back-stage/loginView");
            return false;
        }else {
            //登录成功放行
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
