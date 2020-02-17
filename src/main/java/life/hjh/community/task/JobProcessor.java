package life.hjh.community.task;

import com.alibaba.druid.pool.DruidDataSource;
import life.hjh.community.model.JobInfoWithBLOBs;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Description: spriderJobProject
 * Created by Alves on 2020/3/16 21:01
 */
/*
 * @Component和@Bean都是用来注册Bean并装配到Spring容器中，
 * 但是Bean比Component的自定义性更强。可以实现一些Component实现不了的自定义加载类。
 * */
@Component  /*  实现 webmagic 类*/
public class JobProcessor implements PageProcessor {
    private String url ="https://search.51job.com/list/000000,000000,0000,32%252C01," +
            "9,99,Java,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&" +
            "jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&" +
            "fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
    // 重点 解析页面
    // 解析界面方法 使用css 选择器 参考https://www.runoob.com/cssref/css-selectors.html
    @Override
    public void process(Page page) {
    /*    String html = page.getHtml().toString();
        System.out.println(html);*/

        //解析页面，获取招聘信息详情的url地址
        List<Selectable> list = page.getHtml().css("div#resultList div.el ").nodes(); //#resultList 代表id  .el代表class
        //判断获取到的计划是否为空，
        if (list.size() == 0){
            // 如果为空，表示这是招聘详情页，解析界面，获取招聘详情信息，保存数据
            this.saveJobInfo(page);
        }else {
            //如果不为空,表示这是列表页，解析出详情页的url地址，放到任务队列中
            for (Selectable selectable : list) {
                //获取url地址
                String JobInfoUrl = selectable.links().toString();
                //把获取到的url地址放到任务队列中
                page.addTargetRequest(JobInfoUrl);
                System.out.println(JobInfoUrl);
            }
            //获取下一页的url 方法：就是用Chrome 浏览器 查看元素 使用css 选择器 来获取相应的值
            //div class="p_in" <li class="bk"></li>
            String bkUrl = page.getHtml().css("div.p_in li.bk").nodes().get(1).links().toString();
            //把url 放到任务队列中
            page.addTargetRequest(bkUrl);

        }



    }
    //解析界面，获取招聘详情信息，保存数据
    private void saveJobInfo(Page page) {
        //创建招聘详情对象
        JobInfoWithBLOBs jobInfo = new JobInfoWithBLOBs();
        //解析界面
        Html html = page.getHtml();
        //System.out.println(html);
        //获取数据，封装到对象中
        jobInfo.setCompanyName(html.css("div.cn p.cname a","text").toString());
        jobInfo.setCompanyAddr(Jsoup.parse(html.css("div.bmsg ").nodes().get(1).toString()).text());
        jobInfo.setCompanyInfo(Jsoup.parse(html.css("div.tmsg ").toString()).text());

        jobInfo.setJobName(html.css("div.cn h1","text").toString());
        String jobDetails = html.css("div.cn p.msg", "text").toString();

        // 字符串替换 将gbk 中的 空格ascii 160 换成 utf-8中的 空格 ascii 32
        char newchar = 32;
        char olechar = 160;
        jobDetails =  jobDetails.replace(olechar,newchar);
        //插入详情信息
        jobInfo.setDetails(jobDetails);
        //以多个空格切割字符串
        String[] jobAddrs = StringUtils.split(jobDetails," "); //以空格分隔开，
        jobInfo.setJobAddr(jobAddrs[0]);

        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg ").toString()).text());//.text() 获取内存
        jobInfo.setUrl(page.getUrl().toString());//获取页面链接
        //获取工资
        Integer[] salary = MathSalary.getSalary(html.css("div.cn strong","text").toString());
        jobInfo.setSalaryMin(salary[0]);
        jobInfo.setSalaryMax(salary[1]);
        //获取时间
/*        String time = Jsoup.parse(html.css("div.t1 span").regex(".*发布").toString()).text();
        jobInfo.setTime(time.substring(0,time.length()-2));*/
        String publishTime = jobAddrs[4].replaceAll("发布","");
        /**
         * 字符串转化为时间
         */
        Date date = null;
        SimpleDateFormat simdate = new SimpleDateFormat("yyyy-MM-dd");
        //使用  Calendar 类获取 当前时间 年月日
        Calendar now = Calendar.getInstance();
        String year = now.get(Calendar.YEAR) + "";
        String time = year + "-"+ publishTime;
        try {
            date = simdate.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);
        //日期对象转换为时间戳
        long times = date.getTime();
        System.out.println("时间戳: " + times);
        jobInfo.setTime(times);
        //System.out.println(jobInfo);
        //把结果保存起来  就是把结果保存到了 Pipeline 接口下面的 ResultItems resultItems
        page.putField("jobInfo",jobInfo);
    }

    //爬虫相关设置
    private Site site = Site.me()
            .setCharset("gbk")//设置编码
            .setTimeOut(10 * 1000)//设置超时时间
            .setRetrySleepTime(3000)//设置重试的间隔时间
            .setRetryTimes(3);//设置重试的次数
    @Override
    public Site getSite() {
        return site;
    }

    //注入保存结果集 组件Pipeline
    @Autowired
    private SpringDataPipeline springDataPipeline;

    //这个注解用来定时启动任务
    //initialDelay当任务启动后，等待执行方法的时间 单位ms
    //fixedDelay 每隔多久执行这个方法 单位ms
    //@Scheduled(initialDelay = 1000,fixedDelay = 100*1000)
    //周一到周五  7-8 点 执行 爬虫任务    0 */5 7-8 ? * 2,3,4,5,6
    // test 0 0/1 * ? * *
    //0 */5 8-9 ? * *
    @Scheduled(cron = "0 */5 8-9 ? * *")
    public void process(){
        //Scheduler负责管理待抓取的URL，以及一些去重的工作
        // new QueueScheduler() 抓取的数据存储在内存队列里面
        //new BloomFilterDuplicateRemover(100000) 设置布隆过滤器 处理10万条数据  我怕我的服务器炸了
        Spider.create(new JobProcessor())
                .addUrl(url)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(2))) //1000
                .thread(10)
                .addPipeline(this.springDataPipeline)

                .run();


    }
}
