package life.hjh.community.controller.backStageManagement;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import life.hjh.community.model.User;
import life.hjh.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Description: community
 * Created by Alves on 2020/3/30 1:16
 */
@Controller
public class UserBSController {

    @Autowired
    UserService userService;

    //展示 用户界面
    @RequestMapping("/back-stage/user")
    public String showUser(Model model) {
        //return "redirect:/management/user.html";
        //System.out.println("访问 user.html");
        return "management/user";
    }

    //通过ajax 返回 json 给列表 通过 pagehelper 分页 前端默认传递 limit 和page
    @ResponseBody
    @RequestMapping("/back-stage/showData/user")
    public Map<String,Object> showUserData(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(name = "userName" , required = false, defaultValue = "") String userName,
            @RequestParam(name = "account" , required = false, defaultValue = "") String account
    ) {
        PageInfo<User> userPageInfo = userService.listUser(page, limit,userName,account);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","success");
        long count = userService.getCount();
        map.put("count",count);
        map.put("data",userPageInfo.getList());
        System.out.println(map.toString());
        return map;
    }
    //返回用户添加界面
    @RequestMapping("/back-stage/add/userView")
    public String showAddUser(Model model) {
        return "management/table/addUser";
    }

    // 添加 用户使用 ajax 传递数据
    @ResponseBody
    @RequestMapping(value = "/back-stage/add/user",method = RequestMethod.POST)
    // json的结构和内部字段名称可以与POJO/DTO/javabean完全对应
    public JSONObject addUserData(@RequestBody JSONObject data){

        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        if (data != null){
            JSONObject info = data.getJSONObject("userInfo");
            User user = JSONObject.toJavaObject(info, User.class);//将 json 转换为对象
            user.setAvatarUrl("/images/d-avatar.png");//设置默认头像
            //设置 创建和修改时间
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            //生成 用户token
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            boolean insert = userService.addUser(user);
            if (insert){
                map.put("code","200");
                map.put("msg","插入用户数据成功!");
            }else {
                map.put("code","405");
                map.put("msg","插入用户数据失败!");
            }
        }
        JSONObject json = new JSONObject(map);
        return  json;

    }

    //返回用户修改界面
    @RequestMapping("/back-stage/edit/userView")
    public String showEditUser(Model model) {
        return "management/table/editUser";
    }

    // 修改 用户使用ajax 传递数据
    @ResponseBody
    @RequestMapping(value = "/back-stage/edit/user",method = RequestMethod.POST)
    // json的结构和内部字段名称可以与POJO/DTO/javabean完全对应
    public JSONObject editUserData(@RequestBody JSONObject data){

        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        if (data != null){
            JSONObject info = data.getJSONObject("userInfo");
            User user = JSONObject.toJavaObject(info, User.class);//将 json 转换为对象
            boolean b = userService.editUser(user);
            if (b){
                map.put("code","200");
                map.put("msg","修改用户数据成功!");
            }else {
                map.put("code","405");
                map.put("msg","修改用户数据失败!");
            }
        }
        JSONObject json = new JSONObject(map);
        return  json;

    }

    //删除  用户使用ajax 传递数据
    @ResponseBody //json等内容
    @RequestMapping(value = "/back-stage/delete/user/{id}",method = RequestMethod.DELETE)
    public JSONObject deleteUser(@PathVariable(name = "id") Long id) {
        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        System.out.println(id);
        boolean delete = userService.deleteUserById(id);

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
    @RequestMapping(value = "/back-stage/user/remove",method = RequestMethod.POST)
    public JSONObject  removeUser(@RequestBody Long[] ids) {
        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        boolean delete;
        System.out.println(ids);
        for (Long id : ids) {
            delete = userService.deleteUserById(id);
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
