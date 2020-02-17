package life.hjh.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.hjh.community.dto.ResourceDTO;
import life.hjh.community.exception.CustomizeErrorCode;
import life.hjh.community.exception.CustomizeException;
import life.hjh.community.mapper.ResourceMapper;
import life.hjh.community.mapper.UserMapper;
import life.hjh.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Description: community
 * Created by Alves on 2020/3/25 10:17
 */
@Service
public class ResourceService {

    @Autowired
    ResourceMapper resourceMapper;

    @Autowired
    UserMapper userMapper;

    //插入文件资源
    public void insertResource(Resource resource){

        int insert = resourceMapper.insert(resource);
        if(insert != 1){
            //返回文件上传失败
            throw  new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_ERROR);
        }

    }
    //后台管理      使用pagehelper 返回分页数据
    //显示文件资源列表 通过pagehelper 分页
    public PageInfo<ResourceDTO> listResources(int currentPage, int pageSize,String title){
        PageHelper.startPage(currentPage,pageSize);
        ResourceExample example = new ResourceExample();
        if(title != null && title != "" )
        {
            example.createCriteria().andTitleLike("%"+title+"%");
        }
        List<Resource> resources = resourceMapper.selectByExample(example);

        List<ResourceDTO> resourceDTOS = new ArrayList<>();
/*        for (Resource resource : resources) {
            User user = userMapper.selectByPrimaryKey(resource.getUserId());
            ResourceDTO resourceDTO = new ResourceDTO();
            BeanUtils.copyProperties(resource,resourceDTO);//对象拷贝，把resource拷贝到resourceDTO
            resourceDTO.setUser(user);
            resourceDTOS.add(resourceDTO);
        }*/
        Iterator<Resource> iterator = resources.iterator();
        while (iterator.hasNext()){
            Resource next = iterator.next();
            User user = userMapper.selectByPrimaryKey(next.getUserId());
            ResourceDTO resourceDTO = new ResourceDTO();

            System.out.println(next);
            BeanUtils.copyProperties(next,resourceDTO);//对象拷贝，把resource拷贝到resourceDTO
            resourceDTO.setUser(user);
            resourceDTOS.add(resourceDTO);
        }

        return new PageInfo<ResourceDTO>(resourceDTOS);
    }
    //后台管理  根据 资源id 获取数据
    //显示资源详情 通过资源id 这次要 管理用户进行查询
    public ResourceDTO findResourceById(Long id){
        //查询 资源信息
        ResourceExample example = new ResourceExample();
        example.createCriteria().andIdEqualTo(id);
        List<Resource> list = resourceMapper.selectByExample(example);
        Resource resource = list.get(0);
        ResourceDTO resourceDTO = new ResourceDTO();
        BeanUtils.copyProperties(resource,resourceDTO);
        // 查询用户信息
        User user = userMapper.selectByPrimaryKey(resource.getUserId());
        resourceDTO.setUser(user);
        return resourceDTO;
    }

    /* 后台管理 */

    //插入数据
    public boolean addResource(Resource resource){
        int insert = resourceMapper.insert(resource);
        if (insert ==1 ){
            return true;
        }
        return false;
    }
    //根据 资源id 修改数据
    public boolean editResource(Resource resource){
        ResourceExample resourceExample = new ResourceExample();
        resourceExample.createCriteria().andIdEqualTo(resource.getId());
        int update = resourceMapper.updateByExampleSelective(resource, resourceExample);
        //修改成功
        if(update == 1){
            return true;
        }
        return false;
    }
    //根据 资源id 删除数据
    public boolean deleteResourceById(Long id) {

        int delete = resourceMapper.deleteByPrimaryKey(id);
        //删除成功
        if(delete == 1){
            return true;
        }
        return false;
    }

    //获取文章数目
    public long getCount(){
        long count = resourceMapper.countByExample(new ResourceExample());

        return count;
    }
}
