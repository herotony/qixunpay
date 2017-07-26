package com.qixunpay.frontserver.web.controller;

import com.qixunpay.model.BulkPostData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by wangdalin(闪惠后台研发) on 2017/5/27 9:37.
 */

@Controller
public class IndexController {

    private  static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value={"/"},method = {RequestMethod.HEAD})
    public String head(){

        return "go.jsp";
    }

    @RequestMapping(value={"/index","/"},method = {RequestMethod.GET})
    public String index(Model model) throws Exception{
        model.addAttribute("msg","无语的2017");
        logger.info("I'm here");
        return "go.jsp";
    }

    @RequestMapping(value = {"/bulk"},method = {RequestMethod.POST})
    public String bulk(@ModelAttribute BulkPostData bulkPostData) throws Exception{

        return bulkPostData.getListJsonData().get(0);
    }
}
