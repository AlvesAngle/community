package life.hjh.community.controller.backStageManagement;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import life.hjh.community.model.JobInfoWithBLOBs;
import life.hjh.community.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: community
 * Created by Alves on 2020/3/30 1:16
 */
@Controller
public class JobBSController {

    @Autowired
    JobInfoService jobInfoService;
    //展示 用户界面
    @RequestMapping("/back-stage/job")
    public String showJob(Model model){
        return "management/job";
    }

    //通过ajax 返回 json 给列表 通过 pagehelper 分页 前端默认传递 limit 和page
    @ResponseBody
    @RequestMapping("/back-stage/showData/jobInfo")
    public Map<String,Object> showJobInfoeData(
            @RequestParam(name = "page" ,required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit" ,required = false, defaultValue = "10") int limit,
            @RequestParam(name = "companyName" , required = false, defaultValue = "") String companyName,
            @RequestParam(name = "jobName" , required = false, defaultValue = "") String jobName
    ){
        PageInfo<JobInfoWithBLOBs> jobInfoPageInfo = jobInfoService.listJobInfo(page, limit,companyName,jobName);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","success");
        long count = jobInfoService.getCount();
        map.put("count",count);
        map.put("data",jobInfoPageInfo.getList());
        System.out.println(map);
        return map;
    }
    // 返回编辑界面 使用 model返回
    @RequestMapping(value = "/back-stage/jobInfo/{id}")
    public String getEditJonInfo(@PathVariable(name = "id") Long id,
                                 Model model){
        JobInfoWithBLOBs jobInfo = jobInfoService.findJonInfoById(id);
        model.addAttribute("jobInfo",jobInfo);

        return "management/table/editJobInfo";
    }


    // 添加 用户使用 ajax 传递json数据
    @ResponseBody
    @RequestMapping(value = "/back-stage/add/jobInfo",method = RequestMethod.POST)
    // json的结构和内部字段名称可以与POJO/DTO/javabean完全对应
    public Map<String,Object> addJobInfoData(@RequestBody JSONObject data){

        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        if (data != null){
            JSONObject info = data.getJSONObject("jobInfo");
            JobInfoWithBLOBs jobInfo = JSONObject.toJavaObject(info, JobInfoWithBLOBs.class); //将 json 转换为对象
            boolean b = jobInfoService.addJobInfo(jobInfo);
            if (b){
                map.put("code","200");
                map.put("msg","插入数据成功!");
            }else {
                map.put("code","405");
                map.put("msg","插入数据失败!");
            }
        }
        return  map;

    }

    //跳转到 job 修改界面 editJobInfo
    @RequestMapping("/back-stage/job/edit")
    public String showEditJob(Model model){
        return "management/table/editJobInfo";
    }
    // 修改 用户使用ajax 传递json数据
    @ResponseBody
    @RequestMapping(value = "/back-stage/edit/jobInfo",method = RequestMethod.POST)
    // json的结构和内部字段名称可以与POJO/DTO/javabean完全对应
    public JSONObject editJobInfoData(@RequestBody JSONObject data){

        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        if (data != null){
            JSONObject info = data.getJSONObject("jobInfo");
            JobInfoWithBLOBs jobInfo = JSONObject.toJavaObject(info, JobInfoWithBLOBs.class); //将 json 转换为对象
            boolean b = jobInfoService.editJobInfo(jobInfo);
            if (b){
                map.put("code","200");
                map.put("msg","修改数据成功!");
            }else {
                map.put("code","405");
                map.put("msg","修改数据失败!");
            }
        }
        JSONObject json = new JSONObject(map);
        return  json;

    }
    //删除  job使用ajax 传递json数据
    @ResponseBody //json等内容
    @RequestMapping(value = "/back-stage/delete/jobInfo/{id}",method = RequestMethod.DELETE)
    public JSONObject  deleteJob(@PathVariable(name = "id") Long id) {
        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        System.out.println(id);
        boolean delete = jobInfoService.deleteUserById(id);

        if (delete){
            map.put("code","200");
            map.put("msg","删除数据成功!");
        }else {
            map.put("code","405");
            map.put("msg","删除数据失败!");
        }
        //为了解决 操作成功 却进入不了 ajax success 方法，这里需要 将map转换成 json
        JSONObject json = new JSONObject(map);
        System.out.println(json);
        return json;
    }
    //多列删除 remove
    @ResponseBody //json等内容
    @RequestMapping(value = "/back-stage/jobInfo/remove",method = RequestMethod.POST)
    public JSONObject  removeJob(@RequestBody Long[] ids) {
        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        boolean delete;
        System.out.println(ids);
        for (Long id : ids) {
           delete = jobInfoService.deleteUserById(id);
            if (!delete){
                map.put("code","405");
                map.put("msg","删除数据失败!");
                //为了解决 操作成功 却进入不了 ajax success 方法，这里需要 将map转换成 json
                JSONObject json = new JSONObject(map);
                System.out.println(json);
                return json;
            }
        }
        map.put("code","200");
        map.put("msg","删除数据成功!");
        //为了解决 操作成功 却进入不了 ajax success 方法，这里需要 将map转换成 json
        JSONObject json = new JSONObject(map);
        System.out.println(json);
        return json;
    }
}
