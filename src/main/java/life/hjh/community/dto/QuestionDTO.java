package life.hjh.community.dto;

import life.hjh.community.model.User;
import lombok.Data;

/**
 * Description: community
 * Created by Alves on 2020/2/23 15:48
 * question表 外键关联user 表 查询结果
 */
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;//内容
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator; /*外键 关联 user表 id*/
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String  tag;
    private User user;  /*外键关系*/
}
