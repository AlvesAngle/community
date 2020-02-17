package life.hjh.community;

import life.hjh.community.mapper.JobInfoMapper;
import life.hjh.community.mapper.QuestionMapper;
import life.hjh.community.mapper.UserMapper;
import life.hjh.community.model.JobInfoExample;
import life.hjh.community.model.JobInfoWithBLOBs;
import life.hjh.community.model.Question;
import life.hjh.community.model.QuestionExample;
import life.hjh.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/*
* “Cannot resolve symbol XXX”
*解决方法：点击菜单中的 “File” -> “Invalidate Caches / Restart”，然后点击对话框中的 “Invalidate and Restart”，清空 cache 并且重启。语法就会正确的高亮了。
* */
//@RunWith(SpringRunner.class)
@SpringBootTest
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
//@WebAppConfiguration
class CommunityApplicationTests {
//cannot resolve symbol 'runwith'

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("@BeforeClass");
    }


    @Before
    public void before() {
        System.out.println("每个单元测试方法执行之前该方法都会执行一次。。。。before");
    }

    @After
    public void after() {
        System.out.println("每个单元测试方法执行之后该方法都会执行一次。。。。after");
    }


    @AfterClass
    public static void afterClass() {
        System.out.println("@AfterClass");
    }
    @Test
    void contextLoads() {
        System.out.println("开始测试-----------------");
       /* System.out.println(questionMapper.list());*/
        //System.out.println(questionService.list(1,5));
        /*System.out.println(questionMapper.list(0,3));*/
    }

    @Test
    void test_linkmysql(){
        System.out.println("开始测试-----------------");
        //System.out.println(userMapper.findByToken("34d20f29-6e09-420b-a5d9-6c11581e907e"));
        Question question = new Question();
        question.setTitle("测试插入");
        question.setDescription("测试内容description");
        question.setTag("javascript,html");
        question.setCreator(1L);
        question.setCommentCount(0);
        question.setViewCount(0);
        question.setLikeCount(0);

        questionService.createOrUpdate(question);
    }

    @Test
    void test_addCommentCount(){
        System.out.println("开始测试-----------------");
        //System.out.println(userMapper.findByToken("34d20f29-6e09-420b-a5d9-6c11581e907e"));
        questionService.incView(19L);
    }

    //测试切割 文件名字符串
    @Test
    void testSplit(){
        String fileName = "job_info-44104b6a-1bb7-4db9-bc6a-d5e37d59a872.sql";
        String[] strs = StringUtils.split(fileName,".");
        String[] split = StringUtils.split(strs[0], "-");

        String savaFile = split[0]+"." + strs[strs.length-1];
        System.out.println(savaFile);
    }
    @Autowired
    private JobInfoMapper jobInfoMapper;

    @Test
    void testFindAllJob(){
        List<JobInfoWithBLOBs> jobInfoWithBLOBs = jobInfoMapper.selectByExampleWithBLOBs(new JobInfoExample());
        System.out.println("打印jobInfo数据："+jobInfoWithBLOBs.size());
        for (JobInfoWithBLOBs jobInfoWithBLOB : jobInfoWithBLOBs) {
            System.out.println(jobInfoWithBLOB);
        }
        System.out.println("打印jobInfo长度："+jobInfoWithBLOBs.size());
    }

    @Test
    void testFindQuestionJob(){
        //List<Question> questions = questionMapper.selectByExample(new QuestionExample());
        List<Question> questions = questionMapper.selectByExample(new QuestionExample());
        System.out.println("打印questions数据："+questions.size());
        for (Question question : questions) {
            System.out.println(question);
        }
        System.out.println("打印questions长度："+questions.size());
    }
    @Test
    void testGetCount(){

        long count = questionService.getCount();
        System.out.println("测试文章数目：" + count);
    }

    @Test
    void testFindQuestions(){


    }
}
