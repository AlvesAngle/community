package life.hjh.community.dto;

import life.hjh.community.model.User;
import lombok.Data;

/**
 * Description: community
 * Created by Alves on 2020/3/25 18:40
 */
@Data
public class ResourceDTO {

    private Long id;

    private String title;

    private String description;

    private Long userId;

    private String resourceName;

    private String resourceUrl;

    private Long gmtCreate;

    private Long gmtModify;

    private String tag;

    private User user;  /*外键关系*/

}
