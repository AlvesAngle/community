package life.hjh.community.controller.backStageManagement;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import life.hjh.community.cache.TagCache;
import life.hjh.community.dto.QuestionDTO;
import life.hjh.community.model.Question;
import life.hjh.community.model.User;
import life.hjh.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: community
 * Created by Alves on 2020/3/30 1:16
 */
@Controller
public class QuestionBSController {
    @Autowired
    QuestionService questionService;
    //展示 用户界面
    @RequestMapping("/back-stage/question")
    public String showQuestion(Model model){
       // return "forward:/management/question.html";
        return "management/question";
    }

    //通过ajax 返回 json 给列表 通过 pagehelper 分页 前端默认传递 limit 和page
    @ResponseBody
    @RequestMapping("/back-stage/showData/question")
    public Map<String,Object> showQuestionData(
            @RequestParam(name = "page" ,required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit" ,required = false, defaultValue = "10") int limit,
            @RequestParam(name = "title" , required = false, defaultValue = "") String title
    ){
        PageInfo<QuestionDTO> questionDTOPageInfo = questionService.listQuestion(page, limit, title);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code","0");
        map.put("msg","success");
        //每页的数量pageSize * 总页数pages
        long count = questionService.getCount();
        map.put("count",count);
        map.put("data",questionDTOPageInfo.getList());
        //System.out.println(map);
        return map;
    }

    //返回问题添加界面
    @RequestMapping("/back-stage/add/questionView")
    public String showAddQuestion(Model model) {

        //添加标签
        model.addAttribute("tags", TagCache.get());
        return "management/table/addQuestion";
    }
    // 添加 问题使用 ajax 传递数据
    @ResponseBody
    @RequestMapping(value = "/back-stage/add/question",method = RequestMethod.POST)
    public Map<String,Object> addQuestionData(@RequestBody JSONObject data, HttpServletRequest request){

        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        if (data != null){
            JSONObject info = data.getJSONObject("questionInfo");
            //验证登录
            User user = (User)request.getSession().getAttribute("adminUser");
            Question question = JSONObject.toJavaObject(info, Question.class);//将 json 转换为对象
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCreator(user.getId());
            //默认值为零
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            boolean b = questionService.addQuestion(question);
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

    //返回问题修改界面
    @RequestMapping("/back-stage/edit/questionView")
    public String showEditQuestion(Model model) {
        //添加标签
        model.addAttribute("tags", TagCache.get());
        return "management/table/editQuestion";
    }

    // 修改 问题使用ajax 传递数据
    @ResponseBody
    @RequestMapping(value = "/back-stage/edit/question",method = RequestMethod.POST)
    public JSONObject editQuestionData(@RequestBody JSONObject data){

        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        if (data != null){
            JSONObject info = data.getJSONObject("questionInfo");
            Question question = JSONObject.toJavaObject(info, Question.class);//将 json 转换为对象
            question.setGmtModified(System.currentTimeMillis());//更新修改时间
            boolean b = questionService.editQuestion(question);
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


    //删除  问题使用ajax 传递数据
    @ResponseBody //json等内容
    @RequestMapping(value = "/back-stage/delete/question/{id}",method = RequestMethod.DELETE)
    public JSONObject deleteQuestion(@PathVariable(name = "id") Long id) {
        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        System.out.println(id);
        System.out.println(id);
        int delete = questionService.deleteById(id);

        if (delete ==1){
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
    @RequestMapping(value = "/back-stage/question/remove",method = RequestMethod.POST)
    public JSONObject  removeQuestion(@RequestBody Long[] ids) {
        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        int delete;
        System.out.println(ids);
        for (Long id : ids) {
            delete = questionService.deleteById(id);
            if (delete != 1){
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
