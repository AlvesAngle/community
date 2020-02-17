package life.hjh.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.hjh.community.mapper.JobInfoMapper;
import life.hjh.community.model.JobInfo;
import life.hjh.community.model.JobInfoExample;
import life.hjh.community.model.JobInfoWithBLOBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: community
 * Created by Alves on 2020/3/18 10:30
 */
@Service
public class JobInfoService {

    @Autowired
    private JobInfoMapper jobInfoMapper;

    //获取所有的职位数据
    public PageInfo<JobInfo> list(int currentPage, int pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        List<JobInfo> list = jobInfoMapper.selectByExample(new JobInfoExample());
        return new PageInfo<JobInfo>(list);
    }
    //后台管理
    //根据 职位id来获取数据
    public JobInfoWithBLOBs findJonInfoById(Long id) {

        JobInfoExample jobInfoExample = new JobInfoExample();
        jobInfoExample.createCriteria().andIdEqualTo(id);
        List<JobInfoWithBLOBs> list = jobInfoMapper.selectByExampleWithBLOBs(jobInfoExample);
        JobInfoWithBLOBs jobInfo = list.get(0);
        return jobInfo;

    }

    //后台管理
    // 使用pagehelper 返回分页数据
    public PageInfo<JobInfoWithBLOBs> listJobInfo(int currentPage, int pageSize,String companyName,String jobName) {
        PageHelper.startPage(currentPage,pageSize);
        JobInfoExample example = new JobInfoExample();
        if(companyName != null && companyName != "" )
        {
            example.createCriteria().andCompanyNameLike("%"+companyName+"%");
        }else  if(jobName != null && jobName != "" )
        {
            example.createCriteria().andJobNameLike("%"+jobName+"%");
        }
        List<JobInfoWithBLOBs> list = jobInfoMapper.selectByExampleWithBLOBs(example);
        return new PageInfo<JobInfoWithBLOBs>(list);
    }

    //获取文章数目
    public long getCount(){
        long count = jobInfoMapper.countByExample(new JobInfoExample());

        return count;
    }

    //插入数据
    public boolean addJobInfo(JobInfoWithBLOBs jobInfo){
        int insert = jobInfoMapper.insert(jobInfo);
        if (insert ==1 ){
            return true;
        }
        return false;
    }
    //根据 资源id 修改数据
    public boolean editJobInfo(JobInfoWithBLOBs jobInfo) {
        JobInfoExample jobInfoExample = new JobInfoExample();
        jobInfoExample.createCriteria().andIdEqualTo(jobInfo.getId());
        int update = jobInfoMapper.updateByExampleSelective(jobInfo, jobInfoExample);//通过相同的id 来修改数据
        //修改成功
        if(update == 1){
            return true;
        }
        return false;
    }
    //根据 资源id 删除数据
    public boolean deleteUserById(Long id) {

        int delete = jobInfoMapper.deleteByPrimaryKey(id);
        //删除成功
        if(delete == 1){
            return true;
        }
        return false;
    }
}
