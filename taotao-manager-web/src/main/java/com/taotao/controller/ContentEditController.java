package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentEditController {
    @Autowired
    private ContentService contentService;
    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public TaotaoResult updateContent(TbContent tbContent){
        TaotaoResult result = contentService.updateContent(tbContent);
        return result;
    }
    @RequestMapping("/content/delete")
    @ResponseBody
    public TaotaoResult deleteContent(Integer[] ids){
        TaotaoResult result = contentService.deleteContent(ids);
        return result;
    }
}
