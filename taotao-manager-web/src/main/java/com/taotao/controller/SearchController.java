package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.searchs.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {
    @Autowired
    private SearchItemService searchItemService;
    @RequestMapping("/index/importall")
    @ResponseBody
    public TaotaoResult initSearchItems(){
        TaotaoResult result = searchItemService.importAllItems();
        return result;
    }
}
