package com.scwnb.crm.workbench.controller;

import com.scwnb.crm.settings.domain.User;
import com.scwnb.crm.settings.service.UserService;
import com.scwnb.crm.settings.service.impl.UserServiceImpl;
import com.scwnb.crm.utils.DateTimeUtil;
import com.scwnb.crm.utils.PrintJson;
import com.scwnb.crm.utils.ServiceFactory;
import com.scwnb.crm.utils.UUIDUtil;
import com.scwnb.crm.vo.PaginationVO;
import com.scwnb.crm.workbench.domain.Activity;
import com.scwnb.crm.workbench.domain.Clue;
import com.scwnb.crm.workbench.domain.Tran;
import com.scwnb.crm.workbench.service.ActivityService;
import com.scwnb.crm.workbench.service.ClueService;
import com.scwnb.crm.workbench.service.impl.ActivityServiceImpl;
import com.scwnb.crm.workbench.service.impl.ClueServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController extends HttpServlet {

    @Resource
    UserService userService;
    @Resource
    ClueService clueService;
    @Resource
    ActivityService activityService;
/*
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到线索控制器");

        String path = request.getServletPath();

        if("/workbench/clue/getUserList.do".equals(path)){

            getUserList(request,response);

        }else if("/workbench/clue/save.do".equals(path)){

            save(request,response);

        }else if("/workbench/clue/detail.do".equals(path)){

            detail(request,response);

        }else if("/workbench/clue/getActivityListByClueId.do".equals(path)){

            getActivityListByClueId(request,response);

        }else if("/workbench/clue/unbund.do".equals(path)){

            unbund(request,response);

        }else if("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){

            getActivityListByNameAndNotByClueId(request,response);

        }else if("/workbench/clue/bund.do".equals(path)){

            bund(request,response);

        }else if("/workbench/clue/getActivityListByName.do".equals(path)){

            getActivityListByName(request,response);

        }else if("/workbench/clue/convert.do".equals(path)){

            convert(request,response);

        }


    }
*/
    @RequestMapping("convert.do")
    private void convert(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("执行线索转换的操作");

        String clueId = request.getParameter("clueId");

        //接收是否需要创建交易的标记
        String flag = request.getParameter("flag");

        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t = null;

        //如果需要创建交易
        if("a".equals(flag)){

            t = new Tran();

            //接收交易表单中的参数
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();


            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);

        }

        /*

            为业务层传递的参数：

            1.必须传递的参数clueId，有了这个clueId之后我们才知道要转换哪条记录
            2.必须传递的参数t，因为在线索转换的过程中，有可能会临时创建一笔交易（业务层接收的t也有可能是个null）

         */
        boolean flag1 = clueService.convert(clueId,t,createBy);

        if(flag1){

            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");

        }

    }
    @RequestMapping("getActivityListByName.do")

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动列表（根据名称模糊查）");

        String aname = request.getParameter("aname");

        List<Activity> aList = activityService.getActivityListByName(aname);

        PrintJson.printJsonObj(response, aList);

    }
    @RequestMapping("bund.do")

    private void bund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行关联市场活动的操作");

        String cid = request.getParameter("cid");
        String aids[] = request.getParameterValues("aid");

        boolean flag = clueService.bund(cid,aids);

        PrintJson.printJsonFlag(response, flag);

    }
    @RequestMapping("getActivityListByNameAndNotByClueId.do")

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动列表（根据名称模糊查+排除掉已经关联指定线索的列表）");

        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");

        Map<String,String> map = new HashMap<String,String>();
        map.put("aname", aname);
        map.put("clueId", clueId);

        List<Activity> aList = activityService.getActivityListByNameAndNotByClueId(map);

        PrintJson.printJsonObj(response, aList);



    }
    @RequestMapping("unbund.do")

    private void unbund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行解除关联操作");

        String id = request.getParameter("id");


        boolean flag = clueService.unbund(id);

        PrintJson.printJsonFlag(response, flag);

    }
    @RequestMapping("getActivityListByClueId.do")
    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据线索id查询关联的市场活动列表");

        String clueId = request.getParameter("clueId");

        List<Activity> aList = activityService.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response, aList);


    }
    @RequestMapping("detail.do")
    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("跳转到线索详细信息页");

        String id = request.getParameter("id");

        Clue c = clueService.detail(id);

        request.setAttribute("c", c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);


    }

    @RequestMapping("save.do")
    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行线索添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setAddress(address);
        c.setWebsite(website);
        c.setState(state);
        c.setSource(source);
        c.setPhone(phone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setDescription(description);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
        c.setContactSummary(contactSummary);
        c.setCompany(company);
        c.setAppellation(appellation);


        boolean flag = clueService.save(c);

        PrintJson.printJsonFlag(response, flag);

    }
    @RequestMapping("getUserList.do")
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");


        List<User> uList = userService.getUserList();

        PrintJson.printJsonObj(response, uList);

    }
    @RequestMapping("cluePageList.do")
    @ResponseBody
    private PaginationVO<Clue> pageList(String pageNo, String pageSize) {

        System.out.println("进入到查询线索信息列表的操作");

        int pageNoInt = Integer.valueOf(pageNo);
        //每页展现的记录数
        int pageSizeInt = Integer.valueOf(pageSize);
        //计算出略过的记录数
        int skipCount = (pageNoInt-1)*pageSizeInt;

        Map<String,Object> map = new HashMap<String,Object>();

        map.put("skipCount",skipCount);
        map.put("pageSize",pageSizeInt);

        PaginationVO<Clue> vo = clueService.cluePageList(map);

        return vo;

    }

}




































