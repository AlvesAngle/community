package life.hjh.community.controller;

import com.github.pagehelper.PageInfo;
import life.hjh.community.model.JobInfo;
import life.hjh.community.model.JobInfoWithBLOBs;
import life.hjh.community.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Description: community
 * Created by Alves on 2020/3/15 2:13
 * 爬虫 爬取工作显示到前端
 */
@Controller
public class JobController {

    @Autowired
    private JobInfoService jobInfoService;

    //显示 爬去的工作列表
   /// @ResponseBody
    @RequestMapping(value = "/job",method = RequestMethod.GET)
    public String listJob(Model model,                     //PageInfo<JobInfo> 返回json
                              @RequestParam(name = "currentPage",defaultValue = "1")Integer currentPage,
                              @RequestParam(name = "pageSize",defaultValue = "9")Integer pageSize){
        System.out.println(currentPage+ "    "+pageSize);
        PageInfo<JobInfo> jobInfoPageInfo = jobInfoService.list(currentPage, pageSize);
        model.addAttribute("jobInfoPageInfo",jobInfoPageInfo);
        model.addAttribute("navTag","job");//用于判断那个标签选中
        //return jobInfoPageInfo;
        return "job";
    }


    //detail
    //点击进入工作详情
    @GetMapping("/job/{id}")
    public String FindJob(@PathVariable(name = "id")Long id, Model model){

        JobInfoWithBLOBs jobInfo = jobInfoService.findJonInfoById(id);
        System.out.println(jobInfo);
        model.addAttribute("jobInfo",jobInfo);
        return "jobDetail";
    }

}
