package life.hjh.community.dto;

import lombok.Data;

/**
 * Description: community
 * Created by Alves on 2020/2/13 11:13
 */
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;//头像


}
