package life.hjh.community.dto;

import lombok.Data;

/**
 * Description: community
 * Created by Alves on 2020/2/28 9:28
 * 这是 搜索 分页 传递的对象
 */
@Data
public class QuestionQueryDTO {
    private String search;

    private Integer page;
    private Integer size;
/*    private String sort;
    private Long time;
    private String tag;*/
}