package life.hjh.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description: community
 * Created by Alves on 2020/2/5 15:50
 */
@Controller
public class IndexController {
    @GetMapping("/")
    public String  index() {
        return "index";
    }
}
