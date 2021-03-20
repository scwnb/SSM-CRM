package com.scwnb.crm.workbench.service.impl;

import com.scwnb.crm.utils.SqlSessionUtil;
import com.scwnb.crm.workbench.dao.CustomerDao;
import com.scwnb.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;

    public List<String> getCustomerName(String name) {

        List<String> sList = customerDao.getCustomerName(name);

        return sList;
    }
}
















