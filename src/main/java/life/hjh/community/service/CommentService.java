package life.hjh.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.hjh.community.dto.CommentDTO;
import life.hjh.community.enums.CommentTypeEnum;
import life.hjh.community.enums.NotificationStatusEnum;
import life.hjh.community.enums.NotificationTypeEnum;
import life.hjh.community.exception.CustomizeErrorCode;
import life.hjh.community.exception.CustomizeException;
import life.hjh.community.mapper.*;
import life.hjh.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: community
 * Created by Alves on 2020/3/4 18:40
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper  questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;
    /*
    * 事务管理 ，@Transactional声明式事务基于 AOP,将具体业务逻辑与事务处理解耦
    * @Transactional 该方法体，作为一个统一的事务，如果该方法体中任意一个方法执行失败，则，该方法体所以的事务全部回滚
    * */
    @Transactional
    public void insert(Comment comment, User commentator) {
        //判断 是否 在问题回复  ParentId question 表的外键
        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //判断评论类型 如果 不在评论类型值范围 则抛出错误  其中     QUESTION(1), COMMENT(2);
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        //评论
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null){
                throw  new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            //插入评论
            commentMapper.insertSelective(comment);
            //添加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            //创建通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        }else {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //回复问题
            commentMapper.insertSelective(comment);
            //插入 1条评论数目
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //创建通知
            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }

    }



        private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
            //同一个人不通知
            if (receiver == comment.getCommentator()) {
                return;
            }
            Notification notification = new Notification();
            notification.setGmtCreate(System.currentTimeMillis());
            notification.setType(notificationType.getType());
            notification.setOuterid(outerId);
            notification.setNotifier(comment.getCommentator());
            notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
            notification.setReceiver(receiver);
            notification.setNotifierName(notifierName);

            notification.setOuterTitle(outerTitle);
            System.out.println(notification);
            notificationMapper.insert(notification);
        }
    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0){
            return new ArrayList<>();
        }
        // 使用集合 set 不重复。使用lambda 获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        //将set 转换成 list  userIDs
        List<Long> userIDs = new ArrayList<>();
        userIDs.addAll(commentators);


        //获取评论人，并转换为map ，降低时间复杂度
        UserExample userExample = new UserExample();
        //查询在这个list 里面的 用户数据
        userExample.createCriteria().andIdIn(userIDs);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        // 将users 做成一个map 简化时间复杂度
        //将 comment 转换为commentDTOS
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }

    /* 后台管理  */
    //使用pagehelper 返回分页数据
    public PageInfo<CommentDTO> listComments(int currentPage, int pageSize,String commentor){
        CommentExample commentExample = new CommentExample();
        User useri;
        if(commentor != null && commentor != "" )
        {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNameLike("%"+commentor+"%");
            useri = userMapper.selectByExample(userExample).get(0);

            commentExample.createCriteria().andCommentatorEqualTo(useri.getId());
        }
        PageHelper.startPage(currentPage,pageSize);
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            User user = userMapper.selectByPrimaryKey(comment.getCommentator());
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);//对象拷贝，把comment拷贝到commentDTO
            commentDTO.setUser(user);
            commentDTOS.add(commentDTO);
        }
        PageInfo pageInfo = new PageInfo(commentDTOS,pageSize); //返回固定数量
        return pageInfo;
    }

    //获取评论数目
    public long getCount(){
        long count = commentMapper.countByExample(new CommentExample());

        return count;
    }

    // 后台不需要 插入评论
    public boolean addComment(Comment comment){
        int insert = commentMapper.insert(comment);
        if (insert ==1 ){
            return true;
        }
        return false;
    }
    // 修改评论
    public boolean editComment(Comment comment){
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andIdEqualTo(comment.getId());
        int update = commentMapper.updateByExampleSelective(comment, commentExample);
        //修改成功
        if(update == 1){
            return true;
        }
        return false;
    }
    //删除评论
    public boolean deleteCommentById(Long id) {
        int delete = commentMapper.deleteByPrimaryKey(id);
        //删除成功
        if(delete == 1){
            return true;
        }
        return false;
    }


}
