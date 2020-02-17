package life.hjh.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.hjh.community.exception.CustomizeErrorCode;
import life.hjh.community.exception.CustomizeException;
import life.hjh.community.mapper.UserMapper;
import life.hjh.community.model.User;
import life.hjh.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: community
 * Created by Alves on 2020/3/1 16:16
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        //查询 通过 user 对象 的id查询是否存在记录

        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);


        //如果不存在记录 插入数据
        if(users.size() == 0){
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            //如果存在记录 更新数据
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            updateUser.setGmtModified(System.currentTimeMillis());

            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }

    }
    public void loginOrRegister(User user) {
        //查询 通过 user 对象 的id查询是否存在记录
        String password = user.getPassword();
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId()).andPasswordEqualTo(user.getPassword());
        //userExample.or().andAccountIdEqualTo(user.getAccountId());
        //userExample.or().andPasswordEqualTo(user.getPassword());  // andPasswordLike()   里面的内容还可以是 % %
        List<User> users = userMapper.selectByExample(userExample);


        //如果不存在记录 插入数据 注册
        if(users.size() == 0){
            user.setGmtCreate(System.currentTimeMillis());

            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl("/images/d-avatar.png");
            userMapper.insert(user);
        }else {
            //如果存在记录 更新数据 登录
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setToken(user.getToken());
            updateUser.setGmtModified(System.currentTimeMillis());

            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }

    }

    //修改密码
    public void updateUser(User user,User objUser) {
        //查询 通过 user 对象 的id查询是否存在记录

        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId()).andPasswordEqualTo(user.getPassword());
        List<User> users = userMapper.selectByExample(userExample);


        //如果不存在记录 返回错误
        if(users.size() == 0){

            throw new CustomizeException(CustomizeErrorCode.USER_UPDATE_ERROR);
        }else {
            //如果存在记录 更新数据 登录
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setToken(user.getToken());
            updateUser.setGmtModified(System.currentTimeMillis());

            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }

    }

    /* 后台管理  */

    //使用pagehelper 返回分页数据
    public PageInfo<User> listUser(int currentPage,int pageSize,String userName,String account) {
        PageHelper.startPage(currentPage,pageSize);
        UserExample example = new UserExample();
        if(userName != null && userName != "" )
        {
            example.createCriteria().andNameLike("%"+userName+"%");
        }else  if(account != null && account != "" )
        {
            example.createCriteria().andAccountIdLike("%"+account+"%");
        }


        List<User> list = userMapper.selectByExample(example);

        PageInfo<User> userPageInfo = new PageInfo<>(list);
        return userPageInfo;
    }

    //获取文章数目
    public long getCount(){
        long count = userMapper.countByExample(new UserExample());

        return count;
    }

    //根据 用户id 获取数据
    public User findUserById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }
    //插入数据
    public boolean addUser(User user){
        int insert = userMapper.insert(user);
        //插入成功
        if (insert ==1 ){
            return true;
        }
        return false;
    }
    //根据 用户id 编辑数据
    public boolean editUser(User user) {

        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(user.getId());
        int update = userMapper.updateByExampleSelective(user, userExample);//通过相同的id 来插入数据
        //修改成功
        if(update == 1){
            return true;
        }
        return false;
    }
    //根据 用户id 删除数据
    public boolean deleteUserById(Long id) {

        int delete = userMapper.deleteByPrimaryKey(id);
        //删除成功
        if(delete == 1){
            return true;
        }
        return false;
    }
    //后台登录
    public Map<String,Object> loginBS(User user){
        //查询 通过 user 对象 的id查询是否存在记录

        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId()).andPasswordEqualTo(user.getPassword());
        List<User> users = userMapper.selectByExample(userExample);

        //用于保存 返回数据
        Map<String,Object> map = new HashMap<String, Object>();
        //如果不存在记录 请联系管理员开通账号
        if(users.size() == 0){
            map.put("code","405");
            map.put("msg","账号或密码错误！");
        }else {
            //如果存在记录 更新数据
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            updateUser.setGmtModified(System.currentTimeMillis());

            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);

            map.put("code","200");
            map.put("msg","登录成功");
        }
        return map;
    }

    public boolean editPassword(String oldPassword, User user) {

        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(user.getId()).andPasswordEqualTo(oldPassword);
        int update = userMapper.updateByExampleSelective(user, userExample);//通过相同的id 来插入数据
        //修改成功
        if(update == 1){
            return true;
        }
        return false;
    }
}
