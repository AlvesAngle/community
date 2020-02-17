package life.hjh.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.hjh.community.dto.PaginationDTO;
import life.hjh.community.dto.QuestionDTO;
import life.hjh.community.dto.QuestionQueryDTO;
import life.hjh.community.exception.CustomizeErrorCode;
import life.hjh.community.exception.CustomizeException;
import life.hjh.community.mapper.QuestionExtMapper;
import life.hjh.community.mapper.QuestionMapper;
import life.hjh.community.mapper.UserMapper;
import life.hjh.community.model.Question;
import life.hjh.community.model.QuestionExample;
import life.hjh.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: community
 * Created by Alves on 2020/2/23 15:50
 */
@Service/*可以同时使用questionMapper和UserMapper 起到组装的作用·*/
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;
    // 个人博客文章 分页操作 同时集成了收搜功能
    public PaginationDTO list(String search, Integer page, Integer size) {
        //如果查询关键词不为空 则
        if(StringUtils.isNotBlank(search)){
            String[] tags = StringUtils.split(search," "); //以空格分隔开，
            search = Arrays.stream(tags).collect(Collectors.joining("|")); // 然后用插入 |
        }

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;// 页数 = 总记录数目 / size(页数) + 1


        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);// 求总记录数
        //求模
        if(totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size +1;
        }

        if(page<1){
            page =0;
        }
        if(page > totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);
        // 页码起始位置：size*(page -1)
        Integer offset =page < 1 ? 0 : size * (page - 1);

        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(page);
        /*查询获取分页 数据*/
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question: questions) {
            User user =  userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO =  new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//对象拷贝，把question拷贝到questionDTO
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        /*获取分页数据 封装到 一个类 然后传到前端*/
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }
    //方法的重写 某个用户的博客
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        Integer totalPage;
        PaginationDTO paginationDTO = new PaginationDTO();
        /*Integer totalCount = questionMapper.countByUserId(userId);*/
        /*创建 question mybatis自动生成类*/
        QuestionExample questionExample = new QuestionExample();
        //添加 select count(1) from question where creator_id = #{userId}  的参数id
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);// 求总记录数
        //求模
        if(totalCount % size == 0){
            totalPage = totalCount;
        }else {
            totalPage = totalCount / size +1;
        }

        if(page<1){
            page =1;
        }
        if(page > totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);
        // 页码起始位置：size*(page -1)
        Integer offset = size*(page-1);
        /*查询获取分页 数据*/
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId); //  select * from question where creato=#{userId} limit offset,size 加入约束条件 userId
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question: questions) {
            User user =  userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO =  new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//对象拷贝，把question拷贝到questionDTO
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        /*获取分页数据 封装到 一个类 然后传到前端*/
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }
    //后台管理   根据 资源id 获取数据
    public QuestionDTO getById(Long id) {
        //关联查询 question 和user 表
        Question question = questionMapper.selectByPrimaryKey(id);

        if(question == null){
            throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user =  userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);


        return questionDTO;
    }

    //后台管理   插入数据
        //创建或者更新 问题
    public void createOrUpdate(Question question) {


        //判断id是否为空
        if(question.getId() == null){
            //id 为空 则说明是第一次创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }else {
            //id 不为空 则说明 需要更新数据
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());  //通过相同的id 来插入数据
            int update = questionMapper.updateByExampleSelective(question, example);
            if(update != 1){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }

    }
    //用户浏览 页面添加阅读数
    //问题： 悲观锁 乐观锁，也就是说多人同时请求这个服务， 容易造成 并发 ，线程不安全
    public void incView(Long id) {
        //通过id 获取question列表 的数据
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);

    }
    //获取相关问题
    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        //判断标签是否为空
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays
                .stream(tags)
                .filter(StringUtils::isNotBlank)
                .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
    // 获取热门消息
    public PageInfo<QuestionDTO> getHotQuestion(int currentPage,int pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("view_count desc"); //按创建时间排序  降序
        List<Question> questions = questionMapper.selectByExample(example);
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//对象拷贝，把question拷贝到questionDTO
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }

        PageInfo<QuestionDTO> questionDTOPageInfo = new PageInfo<>(questionDTOS);
        return questionDTOPageInfo;
    }
    /* 后台管理  */
    //使用pagehelper 返回分页数据
    public PageInfo<QuestionDTO> listQuestion(int currentPage,int pageSize,String title) {
        PageHelper.startPage(currentPage,pageSize);
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        QuestionExample example = new QuestionExample();
        if(title != null && title != "" )
        {
            example.createCriteria().andTitleLike("%"+title+"%");
        }
        List<Question> questions = questionMapper.selectByExample(example);
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//对象拷贝，把question拷贝到questionDTO
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }

        PageInfo<QuestionDTO> questionDTOPageInfo = new PageInfo<>(questionDTOS);
        return questionDTOPageInfo;
    }
    //获取文章数目
    public long getCount(){
        long count = questionMapper.countByExample(new QuestionExample());

        return count;
    }
    //插入数据
    public boolean addQuestion(Question question){
        int insert = questionMapper.insert(question);
        if (insert ==1 ){
            return true;
        }
        return false;
    }
    //根据 资源id 修改数据
    public boolean editQuestion(Question question) {

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(question.getId());
        int update = questionMapper.updateByExampleSelective(question, questionExample);//通过相同的id 修改数据
        //修改成功
        if(update == 1){
            return true;
        }
        return false;
    }
    //删除问题
    public int deleteById(Long id) {
        int delete = questionMapper.deleteByPrimaryKey(id);
        //当且仅当 delete 为 1时操作成功
        return delete;
    }
}
