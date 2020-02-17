package life.hjh.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("life.hjh.community.mapper" ) /*配置mybatis Scanning for mappers*/
@EnableScheduling //开启定时任务
@ImportResource(locations = {"classpath:kaptcha.xml"}) //导入验证码 配置文件
public class CommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}
