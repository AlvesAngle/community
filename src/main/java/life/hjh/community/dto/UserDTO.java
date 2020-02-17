package life.hjh.community.dto;

import lombok.Data;

/**
 * Description: community
 * Created by Alves on 2020/4/6 21:07
 * 用户登录dto
 */
@Data
public class UserDTO {
    private String accountId;//账号
    private String password;// 密码
    private String captcha;// 验证码
}
