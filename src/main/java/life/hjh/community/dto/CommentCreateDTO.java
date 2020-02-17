package life.hjh.community.dto;

import lombok.Data;

/**
 * Description: community
 * Created by Alves on 2020/3/4 14:17
 */
//接受来自前端的数据 序列化返回
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
