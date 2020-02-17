package life.hjh.community.controller;

import com.github.pagehelper.PageInfo;
import life.hjh.community.dto.PaginationDTO;
import life.hjh.community.dto.QuestionDTO;
import life.hjh.community.mapper.QuestionMapper;
import life.hjh.community.mapper.UserMapper;
import life.hjh.community.model.Question;
import life.hjh.community.model.User;
import life.hjh.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Description: community
 * Created by Alves on 2020/2/5 15:50
 */
@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String  index(HttpServletRequest request,
                         Model model,
                         @RequestParam(name = "page",defaultValue = "0")Integer page,
                         @RequestParam(name = "size",defaultValue = "9")Integer size,
                         @RequestParam(name = "search",required = false)String search) {
       /*获取cookie 的方法放在拦截器完成*/

        //获取分页属性
        PaginationDTO pagination = questionService.list(search,page,size);
        model.addAttribute("pagination",pagination);
        model.addAttribute("search",search);
        model.addAttribute("navTag","index");
        // 获取最热消息 预览数最多的十个
        PageInfo<QuestionDTO> hotQuestions = questionService.getHotQuestion(1, 10);
        model.addAttribute("hotQuestion",hotQuestions.getList());
        return "index";
    }



}
