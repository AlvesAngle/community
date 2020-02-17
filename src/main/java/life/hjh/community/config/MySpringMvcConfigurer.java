package life.hjh.community.config;

/**
 * Description: community
 * Created by Alves on 2020/4/18 23:25
 */
//@Configuration
public class MySpringMvcConfigurer {
    //所有的WebMvcConfigurer组件都会一起起作用
/*    @Bean //将当前组件添加到容器当前才可生效
    public WebMvcConfigurer webMvcConfigurer() {
        WebMvcConfigurer webMvcConfigurer = new WebMvcConfigurer() {
            //添加视图控制
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("main/login");
                registry.addViewController("/index.html").setViewName("main/login");
                registry.addViewController("/main.html").setViewName("main/index");
            } //添加拦截器
            @Override

            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new MainBSInterceptor())
                        // 拦截所有请求 /**
                        .addPathPatterns("/**")
                        // 排除不需要拦截的请求
                        .excludePathPatterns("/", "/index.html", "/login"
                        //SpringBoot2+中要排除静态资源路径, 因访问时不会加/static，所以配置如下
                                , "/css/**", "/img/**", "/js/**");
            }
        };
        return webMvcConfigurer;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocaleResolver();
    }*/
}
