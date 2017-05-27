package com.qixunpay.frontserver.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by wangdalin(闪惠后台研发) on 2017/5/27 9:37.
 */

@Controller
public class IndexController {

    @RequestMapping(value={"/"},method = {RequestMethod.HEAD})
    public String head(){

        return "go.jsp";
    }

    @RequestMapping(value={"/index","/"},method = {RequestMethod.GET})
    public String index(Model model) throws Exception{
        model.addAttribute("msg","无语的2017");
        return "go.jsp";
    }
}
