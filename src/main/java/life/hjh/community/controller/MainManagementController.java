package life.hjh.community.controller;

import life.hjh.community.cache.TagCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description: community
 * Created by Alves on 2020/3/30 1:07
 *  //后台管理主页
 */
@Controller
public class MainManagementController {
    @RequestMapping("/back-stage/main")
    public String showMain(Model model){
        return "/main";
    }
    @RequestMapping("/back-stage/welcome")
    public String showWelcome(Model model){
        return "management/welcome";
    }

}
