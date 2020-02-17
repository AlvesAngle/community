package life.hjh.community.controller;

import life.hjh.community.dto.CommentCreateDTO;
import life.hjh.community.dto.CommentDTO;
import life.hjh.community.dto.ResultDTO;
import life.hjh.community.enums.CommentTypeEnum;
import life.hjh.community.exception.CustomizeErrorCode;
import life.hjh.community.model.Comment;
import life.hjh.community.model.User;
import life.hjh.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description: community
 * Created by Alves on 2020/3/4 14:00
 */
@Controller
public class CommentController {


    @Autowired
    private CommentService commentService;


    //使用 Ajax 异步请求 传输json
    /*
    * 前端传过来请求的时候，拿到一个json
    * 服务端拿到这个json 之后，反序列化成一个对象 ，然后操作
    * 服务器 返回前端 就是把对象 序列化成json
    *@RequestBody CommentCreateDTO commentCreateDTO 自动完成序列化
    * */
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object  post(@RequestBody CommentCreateDTO commentCreateDTO,
                        HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            //异常处理，返回未登录的信息
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            //处理 评论为空 异常，返回错误信息
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTRY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        commentService.insert(comment,user);
        return ResultDTO.okOf(); //返回评论成功的消息
    }
    /*二级回复*/
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List> comments(@PathVariable(name = "id") Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
