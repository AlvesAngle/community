package life.hjh.community.interceptor;

import life.hjh.community.mapper.UserMapper;
import life.hjh.community.model.User;
import life.hjh.community.model.UserExample;
import life.hjh.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Description: community
 * Created by Alves on 2020/2/29 14:05
 */
@Service /*该接口spring不接管
不可以使用  @Autowired 注解注入
拦截器
*/
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*加载前过滤， 具体api用法请参考 https://docs.spring.io/spring/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-handlermapping-interceptor*/
        Cookie[] cookies = request.getCookies();

        if(cookies !=null && cookies.length !=0){
            for(Cookie cookie :cookies){
                if(cookie.getName().equals("token")){ //用户端登录 写入 session
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample(); //mybati 自动生成代码
                    userExample.createCriteria().andTokenEqualTo(token);  // 该example 会自动生成底层的mapper
                    List<User> users = userMapper.selectByExample(userExample);//使用方法

                    if (users.size() != 0){
                        //设置session
                        HttpSession session = request.getSession();
                        request.getSession().setAttribute("user",users.get(0));
                        Long unreadCount = notificationService.unreadCount(users.get(0).getId());
                        session.setAttribute("unreadCount", unreadCount);
                    }
                    break;
                }else if (cookie.getName().equals("adminToken")){ //后台管理端登录 写入 session
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample(); //mybati 自动生成代码
                    userExample.createCriteria().andTokenEqualTo(token);  // 该example 会自动生成底层的mapper
                    List<User> users = userMapper.selectByExample(userExample);//使用方法

                    if (users.size() != 0){
                        //设置session
                        HttpSession session = request.getSession();
                        request.getSession().setAttribute("adminUser",users.get(0));
                    }
                    break;
                }
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
