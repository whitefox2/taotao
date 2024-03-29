package com.taotao.controller;

import com.taotao.common.pojo.EasyUIResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem findItem(@PathVariable long itemId){
        TbItem item = itemService.getItemById(itemId);
        return item;
    }
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIResult getItemList(Integer page,Integer rows){
        EasyUIResult result = itemService.getItemList(page, rows);
        return result;
    }
    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public TaotaoResult deleteItem(Integer[] ids){
        TaotaoResult result = itemService.deleteItems(ids);
        return result;
    }
    @RequestMapping("/rest/page/item-edit/{ids}")
    public void showUpdate(@PathVariable Integer ids){
        System.out.println(ids);
    }
    @RequestMapping("/item/save")
    @ResponseBody
    public TaotaoResult addItems(TbItem tbItem, String desc,String itemParams){
        TaotaoResult result = itemService.addItems(tbItem, desc, itemParams);
        return result;
    }
    @RequestMapping("/rest/item/instock")
    @ResponseBody
    public TaotaoResult downItem(Integer[] ids){
        TaotaoResult result = itemService.downItem(ids);
        return result;
    }
    @RequestMapping("/rest/item/reshelf")
    @ResponseBody
    public TaotaoResult upItem(Integer[] ids){
        TaotaoResult result = itemService.upItem(ids);
        return result;
    }
    @RequestMapping(value="/rest/item/query/item/desc/{id}")
    @ResponseBody
    public TaotaoResult getItemDesc(@PathVariable("id") Long id) {
        TaotaoResult result = itemService.getItemDesc(id);
        return result;
    }
    @RequestMapping(value="/rest/item/update")
    @ResponseBody
    public TaotaoResult updateItem(TbItem item, String desc) {
        TaotaoResult result = itemService.updateItem(item, desc);
        return result;
    }
}
