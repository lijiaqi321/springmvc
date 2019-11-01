package cn.smbms.controller;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.bill.BillServiceImpl;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/bill")
public class BillController {

    @Resource
    public BillService billService;

    @Resource
    public ProviderService providerService;

    //显示所有订单信息页面
    @RequestMapping("/billlist.html")
    public String billlist(HttpServletRequest request, @RequestParam(value = "queryProductName", required = false) String queryProductName,
                           @RequestParam(value = "queryProviderId", required = false) String queryProviderId,
                           @RequestParam(value = "queryIsPayment", required = false) String queryIsPayment) {

        List<Provider> providerList = new ArrayList<Provider>();

        providerList = providerService.getProviderList("", "");
        request.setAttribute("providerList", providerList);
        if (StringUtils.isNullOrEmpty(queryProductName)) {
            queryProductName = "";
        }

        List<Bill> billList = new ArrayList<Bill>();

        Bill bill = new Bill();
        if (StringUtils.isNullOrEmpty(queryIsPayment)) {
            bill.setIsPayment(0);
        } else {
            bill.setIsPayment(Integer.parseInt(queryIsPayment));
        }

        if (StringUtils.isNullOrEmpty(queryProviderId)) {
            bill.setProviderId(0);
        } else {
            bill.setProviderId(Integer.parseInt(queryProviderId));
        }
        bill.setProductName(queryProductName);
        billList = billService.getBillList(bill);
        request.setAttribute("billList", billList);
        request.setAttribute("queryProductName", queryProductName);
        request.setAttribute("queryProviderId", queryProviderId);
        request.setAttribute("queryIsPayment", queryIsPayment);
        return "billlist";


    }

    //删除订单
    @RequestMapping(value = "/delbill.html", method = RequestMethod.GET)
    public String delBill(HttpServletResponse response, @RequestParam(value = "billid", required = false) String billid) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(billid)) {

            boolean flag = billService.deleteBillById(billid);
            if (flag) {//删除成功
                resultMap.put("delResult", "true");
            } else {//删除失败
                resultMap.put("delResult", "false");
            }
        } else {
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        response.setContentType("application/json");
        return JSONArray.toJSONString(resultMap);

    }

    //显示新增页面
    @RequestMapping("/billadd.html")
    public String addbill(HttpServletRequest request) {
        List<Provider> providerList = new ArrayList<Provider>();
        ProviderService providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList("", "");
        request.setAttribute("list", providerList);
        return "billadd";


    }

    //新增订单
    @RequestMapping("/billadds.html")
    public String addbills(Bill bill, HttpServletRequest request, HttpSession session) {
        bill.setCreatedBy(((User) request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        boolean flag = false;
        ;
        flag = billService.add(bill);
        System.out.println("add flag -- > " + flag);
        if (flag) {
            request.setAttribute("flag", flag);
            return "redirect:/bill/billlist.html";
        } else {
            return "billadd";
        }
    }

    //显示修改订单页面
    @RequestMapping("/billmodify.html")
    public String billmodify(Bill bill,HttpServletRequest request) {
        bill.setModifyBy(((User) request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        boolean flag = false;
        billService = new BillServiceImpl();
        flag = billService.modify(bill);
        if (flag) {
            request.setAttribute("flag", flag);
            return "redirect:/bill/billlist.html";
        } else {
            return "billmodify";
        }


    }
    //根据billid获取对象
    @RequestMapping("/billmodifya.html")
    public String billmodifys(HttpServletRequest request, @RequestParam(value = "billid", required = false) String billid) {


        List<Provider> providerList = new ArrayList<Provider>();
        ProviderService providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList("", "");
        request.setAttribute("list", providerList);

        Bill bill = null;
        bill = billService.getBillById(billid);
        request.setAttribute("bill", bill);
        return "billmodify";


    }

    //查看
    @RequestMapping("/billview.html")
    public String viewBill(HttpSession session, @RequestParam(value = "billid", required = false) String billid) {

            //调用后台方法得到user对象
            Bill bill = null;
            bill = billService.getBillById(billid);
            session.setAttribute("bill", bill);
            return "billview";

    }
    //删除订单操作
    @RequestMapping("/deBill.html")
    public String deBill(HttpSession session,@RequestParam(value = "billid", required = false) String billid){

        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(billid)){

            boolean flag = billService.deleteBillById(billid);
            if(flag){//删除成功
                resultMap.put("delResult", "true");
                session.setAttribute("flag",flag);
                return "redirect:/bill/billlist.html";
            }else{//删除失败
                resultMap.put("delResult", "false");

            }
        }else{
            resultMap.put("delResult", "notexit");
        }

        return "redirect:/bill/billlist.html";

    }
}