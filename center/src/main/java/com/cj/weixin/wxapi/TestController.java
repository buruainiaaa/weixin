package com.cj.weixin.wxapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ${DESCRIPTION}
 *
 * @author cody
 * @create 2018-03-26 16:49
 **/
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("index")
    public String get(HttpServletRequest request, HttpServletResponse response){

        return "redirect:http://www.baidu.com";
    }

}
