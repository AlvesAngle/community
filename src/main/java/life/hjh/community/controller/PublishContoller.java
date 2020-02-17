package life.hjh.community.controller;

import life.hjh.community.cache.TagCache;
import life.hjh.community.dto.QuestionDTO;
import life.hjh.community.mapper.QuestionMapper;
import life.hjh.community.model.Question;
import life.hjh.community.model.User;
import life.hjh.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: community
 * Created by Alves on 2020/2/19 16:37
 */
@Controller
public class PublishContoller {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public  String publish(Model model) {
        //添加标签
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,  //使用thymeleaf接受 来自前端的模板
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request,
            Model model){
        //获取返回值
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());
        if (title==null || title=="") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description==null || description=="") {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (tag==null|| tag=="") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        //判断标签合法性
        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }
        //验证登录
        User user = (User)request.getSession().getAttribute("user");

        if (user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id); //如果有传入的id 那就更新
        //创建或者更新 问题
        questionService.createOrUpdate(question);
        /*questionMapper.create(question);*/
        return "redirect:/";
    }
    /*编辑 问题*/
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model){
        // 通过id 查询 将需要修改的参数
        QuestionDTO question = questionService.getById(id);
        //将数据传入编辑界面
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId()); // 主键id


        //添加标签
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

}
