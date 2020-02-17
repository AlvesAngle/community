package life.hjh.community.exception;

/**
 * Description: community
 * Created by Alves on 2020/3/4 10:25
 * 用于处理Question 的 public QuestionDTO getById(Integer id)方法
 */
public class CustomizeException extends RuntimeException{
    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();

    }


    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
