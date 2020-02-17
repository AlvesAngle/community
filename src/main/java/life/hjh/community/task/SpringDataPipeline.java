package life.hjh.community.task;


import life.hjh.community.mapper.JobInfoMapper;
import life.hjh.community.model.JobInfo;
import life.hjh.community.model.JobInfoExample;
import life.hjh.community.model.JobInfoWithBLOBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDataPipeline implements Pipeline {

    @Autowired
    private JobInfoMapper jobInfoMapper;


    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取封装好的招聘详情对象
        JobInfoWithBLOBs jobInfo = resultItems.get("jobInfo");

        //判断数据是否不为空
        if (jobInfo != null) {
            //如果不为空把数据保存到数据库中
            jobInfoMapper.insert(jobInfo);
        }
    }
}
