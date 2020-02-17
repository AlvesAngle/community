package life.hjh.community.enums;

import com.sun.xml.internal.ws.runtime.config.TubelineFeatureReader;

/**
 * Description: community
 * Created by Alves on 2020/3/4 18:14
 * 该枚举 用于 验证 回复的类型 看是一级回复 QUESTION(1)  还是二级回复COMMENT(2)
 */
public enum  CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);
    private Integer type;

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for(CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()){
            if(commentTypeEnum.getType() == type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }
}
