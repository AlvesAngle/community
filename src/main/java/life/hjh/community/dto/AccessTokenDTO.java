package life.hjh.community.dto;

import lombok.Data;

/**
 * Description: community
 * Created by Alves on 2020/2/12 14:03
 *
 * 用于获取 GitHub 返回的信息
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
     /* 快捷键 ： 按住： alt + insert*/
    public String getClient_id() {
        return client_id;
    }

}
