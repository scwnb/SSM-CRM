package com.scwnb.crm.workbench.service;

import com.scwnb.crm.vo.PaginationVO;
import com.scwnb.crm.workbench.domain.Clue;
import com.scwnb.crm.workbench.domain.Tran;

import java.util.Map;


public interface ClueService {
    boolean save(Clue c);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);


    boolean convert(String clueId, Tran t, String createBy);

    PaginationVO<Clue> cluePageList(Map<String, Object> map);
}
