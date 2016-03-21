package com.school.bookstore.user.service;

import com.school.bookstore.user.dao.UserDao;
import com.school.bookstore.user.domain.User;

/**
 * Created by huangqiuhua on 2015/12/26.
 */
public class UserService {

    private  UserDao userDao = new UserDao();

    /**
     * 完成用户注册
     * @param form
     * @throws UserException
     */
    public void regist(User form) throws UserException{
        User user = userDao.findByUsername(form.getUsername());
        if(user != null)
            throw new UserException("此用户名已被注册");
        user = userDao.findByEmail(form.getEmail());
        if(user != null)
            throw new UserException("此Email已被注册");
        userDao.add(form);
    }

    /**
     * 完成用户激活功能
     * @param code
     * @throws UserException
     */
    public void active(String code) throws UserException {
        User user = userDao.findByCode(code);
        if(user == null)
            throw  new UserException("激活码无效！");
        if (user.isState())
            throw  new UserException("您已经激活过了，切勿重复激活！");
        userDao.updateState(user.getUid(), true);

    }

    /**
     * 登录功能
     * @param form
     * @return
     * @throws UserException
     */
    public User login(User form) throws UserException {
        User user = userDao.findByUsername(form.getUsername());
        if(user == null)
            throw new UserException("此用户名不存在！");
        if(!user.getPassword().equals(form.getPassword()))
            throw new UserException("密码错误，请重新输入。");
        if(!user.isState())
            throw new UserException("您还未激活!也就是说是死的!");
        return user;
    }
}
