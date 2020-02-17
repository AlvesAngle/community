package life.hjh.community.dto;

import lombok.Data;

/**
 * Description: community
 * Created by Alves on 2020/4/4 23:54
 * 返回 给前端数据
 */
@Data
public class ResultBeanDTO<T> {

    private Integer code; //返回数据 状态码
    private String msg;  // 返回数据  信息 成功或者错误
    private  T data;       // 返回数据 内容
}
