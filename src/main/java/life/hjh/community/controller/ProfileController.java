package life.hjh.community.controller;

import life.hjh.community.dto.PaginationDTO;
import life.hjh.community.model.User;
import life.hjh.community.service.NotificationService;
import life.hjh.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: community
 * Created by Alves on 2020/2/28 14:49
 */
/*个人界面 api*/
@Controller
public class ProfileController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private QuestionService questionService;

   @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size,
                          Model model){

        //验证登录 , 服务器启动时已经 写入了session
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return "redirect:/";
        }

        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            PaginationDTO pagination = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination",pagination);

        }else if ("replies".equals(action)){
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("section", "replies");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "最新回复");
            System.out.println(paginationDTO);
        }


       return "profile";
    }
}
