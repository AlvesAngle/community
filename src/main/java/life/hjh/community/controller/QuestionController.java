package life.hjh.community.controller;

import life.hjh.community.dto.CommentDTO;
import life.hjh.community.dto.QuestionDTO;
import life.hjh.community.enums.CommentTypeEnum;
import life.hjh.community.exception.CustomizeErrorCode;
import life.hjh.community.exception.CustomizeException;
import life.hjh.community.service.CommentService;
import life.hjh.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Description: community
 * Created by Alves on 2020/3/1 11:05
 */
@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    //评论功能
    @Autowired
    private CommentService commentService;

    /*详情页面编写 前面的文章预览列表点击 文章，传递文章id  查询信息 返回到详情页*/
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Long id, Model model){
        /*System.out.println("获取后端保存的session "+request.getSession().getAttribute("user")); // HttpServletRequest request*/
        //获取 question 和user 表联合查询的信息
        QuestionDTO questionDTO = questionService.getById(id);
        //获取相关问题
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        //获取 comment 和user 表联合查询的信息
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);

        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }

    //删除问题

    @RequestMapping(name = "/question/delete/{id}")
    public String delete(@PathVariable Long id,
                         Model model){
        int delete = questionService.deleteById(id);
        if(delete ==1){
            model.addAttribute("msg", "<script>alert('删除成功！')</script>");
        }else if (delete !=1 ){

            throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);

        }
        return "profile";
    }
}
