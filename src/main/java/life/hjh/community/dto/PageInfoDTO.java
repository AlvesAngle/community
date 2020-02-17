package life.hjh.community.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description: community
 * Created by Alves on 2020/3/29 18:26
 * 用于返回前端的数据 json
 */
@Data
public class PageInfoDTO<T> implements Serializable {
    private String code;
    private String msg;
    private Integer count;
    private List<T> data; // 用于保存 数据表的数据
}
