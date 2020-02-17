package life.hjh.community.controller;

import life.hjh.community.dto.FileDTO;
import life.hjh.community.dto.SaveFileDTO;
import life.hjh.community.provider.AliCloudProvider;
import life.hjh.community.provider.UCloudProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Description: community
 * Created by Alves on 2020/3/9 14:40
 */
@Controller
public class FileController {

    @Autowired
    private UCloudProvider uCloudProvider;
    @Autowired
    private AliCloudProvider aliCloudProvider;

    //图片上传到 ucloud 对象云存储
    @RequestMapping("/file/images")
    @ResponseBody  //如果想要有返回结果必须要 写@ResponseBody
    public FileDTO uploadImage(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        FileDTO fileDTO = new FileDTO();
        try {
            String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());

            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileName);
            fileDTO.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileDTO;
    }


/*    @RequestMapping("/download")
    public void downLoadfile(HttpServletResponse response) throws Exception{
        String fileName = "job_info-44104b6a-1bb7-4db9-bc6a-d5e37d59a872.sql";
        System.out.println(fileName);
        //将文件名传递过去，ucloud 下载
        //通知浏览器以附件形式下载
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(fileName.getBytes(), "UTF-8"));
        uCloudProvider.downloadFile(fileName);
    }*/
}
