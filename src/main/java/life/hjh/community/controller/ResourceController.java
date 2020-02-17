package life.hjh.community.controller;

import com.github.pagehelper.PageInfo;
import life.hjh.community.cache.TagCache;
import life.hjh.community.dto.FileDTO;
import life.hjh.community.dto.ResourceDTO;
import life.hjh.community.dto.SaveFileDTO;
import life.hjh.community.model.Resource;
import life.hjh.community.model.User;
import life.hjh.community.provider.UCloudProvider;
import life.hjh.community.service.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Description: community
 * Created by Alves on 2020/3/25 15:09
 */
@Controller
public class ResourceController {
    @Autowired
    private UCloudProvider uCloudProvider;
    @Autowired
    private ResourceService resourceService;
    //显示上传页面
    /*分享文件页面
     * 获取share.html页面
     * */
    @RequestMapping("/resource/share")
    public String shareFile(Model model){
        //添加标签
        model.addAttribute("tags", TagCache.get());
        return "/share";
    }
    //显示资源列表

    @RequestMapping(value = "/resource/list",method = RequestMethod.GET)
    public String listResource(Model model,                     //PageInfo<JobInfo> 返回json
                               @RequestParam(name = "currentPage",defaultValue = "1")Integer currentPage,
                               @RequestParam(name = "pageSize",defaultValue = "9")Integer pageSize,
                               @RequestParam(name = "title" , required = false, defaultValue = "") String title){
        PageInfo<ResourceDTO> resourcePageInfo = resourceService.listResources(currentPage, pageSize,title);
        model.addAttribute("resourcePageInfo",resourcePageInfo);
        model.addAttribute("navTag","resource");//用于判断那个标签选中
        return "resources";
    }

    //显示资源详情下载页面
    @RequestMapping("/resource/download/{id}")
    public String downloadFile(Model model,
                               @PathVariable(name = "id")long id){

        // 通过 id 查询 resource
        ResourceDTO resource = resourceService.findResourceById(id);
        model.addAttribute("resource",resource);
        return "resourceDetail";
    }


    //上传资源到 ucloud 对象云存储
    /*
     * 然后把资源的信息保存到数据库
     * id 自增
     * userid 用户表外键
     * title 标题
     *
     * */
    @RequestMapping(value = "/file/upload",method = RequestMethod.POST)
  // @ModelAttribute Resource resource  在from 表单中返回 一个对象
    public String uploadFile(HttpServletRequest request ) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("fileName");
        Resource resource = new Resource();
        resource.setTitle(multipartRequest.getParameter("title"));
        resource.setDescription(multipartRequest.getParameter("description"));
        resource.setTag(multipartRequest.getParameter("tag"));

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
        //获取 分享资源用户的信息 ，来着session
        User user = (User)request.getSession().getAttribute("user");
        // 给 resource 对象赋值
        resource.setUserId(user.getId());
        resource.setGmtCreate(System.currentTimeMillis());
        resource.setGmtModify(resource.getGmtCreate());
        resource.setResourceName(saveFileDTO.getFileName());
        resource.setResourceUrl(saveFileDTO.getUrl());
        //System.out.println("文件上传，插入数据到mysql");
        //将数据保存 到 数据表 resource
        resourceService.insertResource(resource);

        //return fileDTO;
        return "redirect:/";
    }
    /*
     * 文件下载 在资源详情页
     * <a href="/user/test/xxxx.txt" download="文件名.txt">点击下载</a>
     *  点击之后 跳转到 这个controller
     * */

    @RequestMapping("/file/download")
    public void downLoad(HttpServletResponse response, @RequestParam(name = "fileName")String fileName) throws Exception{
        System.out.println(fileName);
        //需要对文件名字进行切割
        String[] strs = StringUtils.split(fileName,".");
        String[] split = StringUtils.split(strs[0], "-");

        String saveName = split[0]+"." + strs[strs.length-1];
        System.out.println(saveName);

        //获取当前用户 下载目录名
        Map<String, String> map = System.getenv();
        String userName = map.get("USERNAME");// 获取用户名
        String localDir = "C:/Users/"+userName+"/Downloads";


        //通知浏览器以附件形式下载
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(fileName.getBytes(), "UTF-8"));
        //将文件名传递过去，ucloud 下载
        uCloudProvider.downloadFile(fileName,localDir,saveName);
    }

}
