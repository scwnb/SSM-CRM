package com.scwnb.crm.settings.service;

import com.scwnb.crm.exception.LoginException;
import com.scwnb.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
