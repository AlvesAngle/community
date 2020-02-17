package life.hjh.community.dto;

import lombok.Data;

import java.util.List;

/**
 * Description: community
 * Created by Alves on 2020/3/8 14:32
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;

}
