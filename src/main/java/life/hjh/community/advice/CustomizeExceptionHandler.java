package life.hjh.community.advice;


import com.alibaba.fastjson.JSON;
import life.hjh.community.dto.ResultDTO;
import life.hjh.community.exception.CustomizeErrorCode;
import life.hjh.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Description: community
 * Created by Alves on 2020/3/4 9:57
 * 处理错误请求异常
 * 统一操作，所有的错误都在这里处理
 */
/*
* 基于@ControllerAdvice注解的Controller层的全局异常统一处理
* */
@ControllerAdvice
public class CustomizeExceptionHandler{
    //出现错误请求， 返回 json 报错
    @ExceptionHandler(Exception.class)
    Object handle(Throwable e, Model model, HttpServletRequest request, HttpServletResponse response) {
       // HttpStatus status = getType(request);
        String contentType = request.getContentType(); //获取请求 类型 json 或者 html
        if ("application/json".equals(contentType)){ //判断类似是否为 json
            ResultDTO resultDTO;
            //返回 json
            if(e instanceof CustomizeException){
                resultDTO =  ResultDTO.errorOf((CustomizeException)e);
            }else {
                //无法处理异常
                resultDTO =  ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {
                e.printStackTrace();
            }
            return null;
        }else {
            //错误页面跳转
            if(e instanceof CustomizeException){
                //可以处理异常
                model.addAttribute("message",e.getMessage());
            }else {
                //无法处理异常
                model.addAttribute("message",CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }


    }
    //请求错误，返回错误状态码
/*    private HttpStatus getType(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }*/
}
