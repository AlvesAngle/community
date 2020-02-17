package life.hjh.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description: community
 * Created by Alves on 2020/2/29 11:53
 */
@Configuration
@EnableWebMvc
/*WebMvcConfigurer ：这个接口用于 拦截地址 */
public class WebConfig implements WebMvcConfigurer {
    //登录拦截
    @Autowired
    private SessionInterceptor sessionInterceptor;
    //后台管理拦截
    @Autowired
    private bsMainInterceptor bsmainInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
        * addPathPatterns("/**")
        * 过滤规则
        * */
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
        registry.addInterceptor(bsmainInterceptor).addPathPatterns("/back-stage/**").excludePathPatterns("/back-stage/login","/back-stage/loginView","/back-stage/logout");
    }
    /*重写 该方法可以配置 静态文件：css js 资源等， 如果重写，那就前端废了*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }
}