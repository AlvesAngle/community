package life.hjh.community.controller.backStageManagement;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import life.hjh.community.dto.CommentDTO;
import life.hjh.community.model.Comment;
import life.hjh.community.service.CommentService;
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
public class CommentBSController {
    @Autowired
    CommentService commentService;
    //展示 评论界面
    @RequestMapping("/back-stage/comment")
    public String showComment(Model model){
        return "/management/comment";
    }

    //通过ajax 返回 json 给列表 通过 pagehelper 分页 前端默认传递 limit 和page
    @ResponseBody
    @RequestMapping("/back-stage/showData/comment")
    public Map<String,Object> showJobInfoeData(
            @RequestParam(name = "page" ,required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit" ,required = false, defaultValue = "10") int limit,
            @RequestParam(name = "commentor" , required = false, defaultValue = "") String commentor
    ){
        PageInfo<CommentDTO> commentDTOPageInfo = commentService.listComments(page, limit,commentor);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","success");
        long count = commentService.getCount();
        map.put("count",count);
        map.put("data",commentDTOPageInfo.getList());
        System.out.println(map);
        return map;
    }


    //返回评论修改界面
    @RequestMapping("/back-stage/edit/commentView")
    public String showEditComment(Model model) {
        return "management/table/editComment";
    }

    // 修改 评论使用ajax 传递数据
    @ResponseBody
    @RequestMapping(value = "/back-stage/edit/comment",method = RequestMethod.POST)
    // json的结构和内部字段名称可以与POJO/DTO/javabean完全对应
    public JSONObject editCommentData(@RequestBody JSONObject data){

        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        if (data != null){
            JSONObject info = data.getJSONObject("commentInfo");
            Comment comment = JSONObject.toJavaObject(info, Comment.class);//将 json 转换为对象
            boolean b = commentService.editComment(comment);
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

    //删除  评论使用ajax 传递数据
    @ResponseBody //json等内容
    @RequestMapping(value = "/back-stage/delete/comment/{id}",method = RequestMethod.DELETE)
    public JSONObject  deleteComment(@PathVariable(name = "id") Long id) {
        //用于保存 返回数据
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println(id);
        boolean delete = commentService.deleteCommentById(id);

        if (delete) {
            map.put("code", "200");
            map.put("msg", "删除数据成功!");
        } else {
            map.put("code", "405");
            map.put("msg", "删除数据失败!");
        }
        //为了解决 操作成功 却进入不了 ajax success 方法，这里需要 将map转换成 json
        JSONObject json = new JSONObject(map);
        System.out.println(json);
        return json;
    }
    //多列删除 remove
    @ResponseBody //json等内容
    @RequestMapping(value = "/back-stage/comment/remove",method = RequestMethod.POST)
    public JSONObject removeComment(@RequestBody Long[] ids) {
        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        boolean delete;
        System.out.println(ids);
        for (Long id : ids) {
            delete = commentService.deleteCommentById(id);
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
