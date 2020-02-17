package life.hjh.community.dto;

import life.hjh.community.model.User;
import lombok.Data;

/**
 * Description: community
 * Created by Alves on 2020/3/7 0:46
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Integer commentCount;

    private User user;
}
