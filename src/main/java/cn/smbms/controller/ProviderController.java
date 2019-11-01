package cn.smbms.controller;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
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
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/provide")
public class ProviderController {

    @Resource
    public ProviderService providerService;

    //显示所有供应商西信息
    @RequestMapping("/providerlist.html")
    public String providerlist(HttpServletRequest request, @RequestParam(value = "queryProName",required = false)String queryProName,
                               @RequestParam(value = "queryProCode",required = false)String queryProCode  ){


        if(StringUtils.isNullOrEmpty(queryProName)){
            queryProName = "";
        }
        if(StringUtils.isNullOrEmpty(queryProCode)){
            queryProCode = "";
        }
        List<Provider> providerList = new ArrayList<Provider>();
      providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList(queryProName,queryProCode);
        request.setAttribute("providerList", providerList);
        request.setAttribute("queryProName", queryProName);
        request.setAttribute("queryProCode", queryProCode);
       return "providerlist";

    }
    //显示供应商新增页面
    @RequestMapping("/provideradd.html")
    public String provideradd(){
        return "provideradd";


    }

    //新增供应商
    @RequestMapping("/provideradds.html")
    public String provideradds(Provider provider,HttpServletRequest request){

        provider.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        boolean flag = false;
      providerService = new ProviderServiceImpl();
        flag = providerService.add(provider);
        if(flag){
            return "redirect:/provide/providerlist.html";
        }else{
          return "provideradd";
        }

    }
    //显示修改供应商信息
    @RequestMapping("/providermodify.html")
    public String pwdmodify(HttpServletRequest request, @RequestParam(value = "proid",required = false)String proid) {


        Provider provider = null;
        provider = providerService.getProviderById(proid);
        request.setAttribute("provider", provider);
        return "providermodify";
    }

    @RequestMapping(value = "/providermodifys.html",method = RequestMethod.POST)
    public String providermodifys(Provider provider, HttpServletRequest request
           ){
        provider.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        boolean flag = false;
        flag = providerService.modify(provider);
        if(flag){
            return "redirect:/provide/providerlist.html";
        }else{
            return "providermodify";
        }

    }

    //查看供应商信息
    @RequestMapping("/providerview.html")
    public String providerview(HttpServletRequest request,@RequestParam(value = "proid",required = false)String proid ) {



            Provider provider = null;
            provider = providerService.getProviderById(proid);
            request.setAttribute("provider", provider);
            return "providerview";


    }



    //删除
    @RequestMapping("/delprovice.html")
    public String delProvice(HttpServletRequest request,@RequestParam(value = "proid", required = false)String id){


        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){

            int flag = providerService.deleteProviderById(id);
            if(flag == 0){//删除成功
                resultMap.put("delResult", "true");
            }else if(flag == -1){//删除失败
                resultMap.put("delResult", "false");
            }else if(flag > 0){//该供应商下有订单，不能删除，返回订单数
                resultMap.put("delResult", String.valueOf(flag));
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        request.setAttribute("providerService",providerService.deleteProviderById(id));
        return "redirect:/provide/providerlist.html";

    }
}
