package com.taotao.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    /**
     * 我们的页面请求 如果是
     * http://localhost:8082/index.jsp
     *   <welcome-file-list>
             <welcome-file>index.jsp</welcome-file>
             <welcome-file>index.html</welcome-file>
         </welcome-file-list>
        http://localhost:8082/index.html
     */

    @RequestMapping("/index")
    public String showIndex(){
        return "index";
    }
}
