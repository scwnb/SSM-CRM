package com.scwnb.crm.settings.service.impl;

import com.scwnb.crm.settings.dao.DicTypeDao;
import com.scwnb.crm.settings.dao.DicValueDao;
import com.scwnb.crm.settings.domain.DicType;
import com.scwnb.crm.settings.domain.DicValue;
import com.scwnb.crm.settings.service.DicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {

    @Resource
    private DicTypeDao dicTypeDao;
    @Resource
    private DicValueDao dicValueDao;

    public Map<String, List<DicValue>> getAll() {


        Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();

        //将字典类型列表取出
        List<DicType> dtList = dicTypeDao.getTypeList();

        //将字典类型列表遍历
        for(DicType dt : dtList){

            //取得每一种类型的字典类型编码
            String code = dt.getCode();

            //根据每一个字典类型来取得字典值列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            map.put(code+"List", dvList);


        }

        return map;
    }
}




















