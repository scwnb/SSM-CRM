package com.scwnb.crm.settings.service.impl;

import com.scwnb.crm.exception.LoginException;
import com.scwnb.crm.settings.dao.UserDao;
import com.scwnb.crm.settings.domain.User;
import com.scwnb.crm.settings.service.UserService;
import com.scwnb.crm.utils.DateTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String,String> map = new HashMap<String,String>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = userDao.login(map);

        if(user==null){

            throw new LoginException("账号密码错误");

        }

        //如果程序能够成功的执行到该行，说明账号密码正确
        //需要继续向下验证其他3项信息

        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime)<0){

            throw new LoginException("账号已失效");

        }

        //判断锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){

            throw new LoginException("账号已锁定");

        }

        //判断ip地址
        String allowIps = user.getAllowIps();

        if(!allowIps.contains(ip)){

            throw new LoginException("ip地址受限");

        }


        return user;
    }

    public List<User> getUserList() {

        List<User> uList = userDao.getUserList();

        return uList;
    }
}




































