package life.hjh.community.controller.backStageManagement;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import life.hjh.community.cache.TagCache;
import life.hjh.community.dto.FileDTO;
import life.hjh.community.dto.ResourceDTO;
import life.hjh.community.dto.SaveFileDTO;
import life.hjh.community.model.Resource;
import life.hjh.community.model.User;
import life.hjh.community.provider.UCloudProvider;
import life.hjh.community.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: community
 * Created by Alves on 2020/3/30 1:16
 */
@Controller
public class ResourceBSController {

    @Autowired
    private UCloudProvider uCloudProvider;

    @Autowired
    ResourceService resourceService;
    //展示 用户界面
    @RequestMapping("/back-stage/resource")
    public String showResource(Model model){

        return "/management/resource";
    }

    //通过ajax 返回 json 给列表 通过 pagehelper 分页 前端默认传递 limit 和page
    @ResponseBody
    @RequestMapping("/back-stage/showData/resource")
    public Map<String,Object> showResourceData(
            @RequestParam(name = "page" ,required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit" ,required = false, defaultValue = "10") int limit,
            @RequestParam(name = "title" , required = false, defaultValue = "") String title
    ){
        PageInfo<ResourceDTO> resourceDTOPageInfo = resourceService.listResources(page, limit,title);
        Map<String,Object> map = (Map<String, Object>) new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","success");
        long count = resourceService.getCount();
        map.put("count",count);//resourceDTOPageInfo.getPages()*resourceDTOPageInfo.getSize()
        map.put("data",resourceDTOPageInfo.getList());
        return map;
    }
    //返回资源添加界面
    @RequestMapping("/back-stage/add/resourceView")
    public String showAddResource(Model model) {

        //添加标签
        model.addAttribute("tags", TagCache.get());
        return "management/table/addResource";
    }

    // 添加 资源使用 ajax 传递数据
    @ResponseBody
    @RequestMapping(value = "/back-stage/add/resource",method = RequestMethod.POST)
    public JSONObject addResourceData(HttpServletRequest request,@RequestParam("fileName") MultipartFile file,
                                      @RequestParam("title") String title,
                                      @RequestParam("description") String description,
                                      @RequestParam("tag") String tag){

        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        boolean insert=false;
/*        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("fileName");*/
        Resource resource = new Resource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setTag(tag);

        SaveFileDTO saveFileDTO = new SaveFileDTO(); //返回 上传到Ucloud的 文件信息


        FileDTO fileDTO = new FileDTO(); //返回前端的数据
        try {
            saveFileDTO = uCloudProvider.uploadFile(file.getInputStream(), file.getContentType(), file.getOriginalFilename());

            fileDTO.setSuccess(1);
            fileDTO.setUrl(saveFileDTO.getUrl());
            fileDTO.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取 分享资源用户的信息 ，来自session
        User user = (User)request.getSession().getAttribute("adminUser");
        // 给 resource 对象赋值
        resource.setUserId(user.getId());
        resource.setGmtCreate(System.currentTimeMillis());
        resource.setGmtModify(resource.getGmtCreate());
        resource.setResourceName(saveFileDTO.getFileName());
        resource.setResourceUrl(saveFileDTO.getUrl());

        //将数据保存 到 数据表 resource
        insert = resourceService.addResource(resource);
        System.out.println("文件上传，插入数据到mysql");
        if (insert){
            map.put("code","200");
            map.put("msg","插入资源数据成功!");
        }else {
            map.put("code","405");
            map.put("msg","插入资源数据失败!");
        }
        JSONObject json = new JSONObject(map);
        return  json;

    }

    //返回资源修改界面
    @RequestMapping("/back-stage/edit/resourceView")
    public String showEditResource(Model model) {

        //添加标签
        model.addAttribute("tags", TagCache.get());
        return "management/table/editResource";
    }

    // 修改 资源使用ajax 传递数据
    @ResponseBody
    @RequestMapping(value = "/back-stage/edit/resource",method = RequestMethod.POST)
    // json的结构和内部字段名称可以与POJO/DTO/javabean完全对应
    public JSONObject editResourceData(@RequestBody JSONObject data){

        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        if (data != null){
            JSONObject info = data.getJSONObject("resourceInfo");
            Resource resource = JSONObject.toJavaObject(info, Resource.class);//将 json 转换为对象
            resource.setGmtModify(System.currentTimeMillis());
            boolean b = resourceService.editResource(resource);
            if (b){
                map.put("code","200");
                map.put("msg","修改资源数据成功!");
            }else {
                map.put("code","405");
                map.put("msg","修改资源数据失败!");
            }
        }
        JSONObject json = new JSONObject(map);
        return  json;

    }

    //删除  资源使用ajax 传递数据
    @ResponseBody //json等内容
    @RequestMapping(value = "/back-stage/delete/resource/{id}",method = RequestMethod.DELETE)
    public JSONObject deleteResource(@PathVariable(name = "id") Long id) {
        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        System.out.println(id);
        boolean delete = resourceService.deleteResourceById(id);

        if (delete){
            map.put("code","200");
            map.put("msg","删除资源数据成功!");
        }else {
            map.put("code","405");
            map.put("msg","删除资源数据失败!");
        }
        //为了解决 操作成功 却进入不了 ajax success 方法，这里需要 将map转换成 json
        JSONObject json = new JSONObject(map);
        System.out.println(json);
        return json;
    }

    //多列删除 remove
    @ResponseBody //json等内容
    @RequestMapping(value = "/back-stage/resource/remove",method = RequestMethod.POST)
    public JSONObject  removeResource(@RequestBody Long[] ids) {
        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        boolean delete;
        System.out.println(ids);
        for (Long id : ids) {
            delete = resourceService.deleteResourceById(id);
            if (!delete){
                map.put("code","405");
                map.put("msg","删除资源数据失败!");
                //为了解决 操作成功 却进入不了 ajax success 方法，这里需要 将map转换成 json
                JSONObject json = new JSONObject(map);
                System.out.println(json);
                return json;
            }
        }
        map.put("code","200");
        map.put("msg","删除资源数据成功!");
        //为了解决 操作成功 却进入不了 ajax success 方法，这里需要 将map转换成 json
        JSONObject json = new JSONObject(map);
        System.out.println(json);
        return json;
    }
}
