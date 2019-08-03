package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Override
    public TbItem getItemById(Long itemId) {
        return tbItemMapper.findItemById(itemId);
    }

    @Override
    public EasyUIResult getItemList(int page, int rows) {
        //初始化分页设置 他的底层原理是 自动拼接limit语句
        PageHelper.startPage(page, rows);
        //查询所有商品信息
        List<TbItem> items = tbItemMapper.findItemAll();
        //取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(items);
        //ctrl+alt+L  整理代码
        EasyUIResult result = new EasyUIResult();
        //使用插件得到总记录条数
        result.setTotal(pageInfo.getTotal());
        //被分页插件处理以后得到的结果集了集合对象
        result.setRows(items);
        return result;
    }

    @Override
    public TaotaoResult deleteItems(Integer[] ids) {
        int i = tbItemMapper.deleteItems(ids);
        if (i != 0) {
            return TaotaoResult.ok();
        }
        return null;
    }

    @Override
    public TaotaoResult addItems(TbItem tbItem, String desc) {
        Long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItem.setStatus((byte) 1);
        Date date = new Date();
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        //所有数据准备完毕才能添加商品信息
        int itemCount = tbItemMapper.addItems(tbItem);
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(itemId);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        //准备了描述信息的所有数据才能添加描述信息
        int itemDescCount = tbItemDescMapper.addItemDesc(itemDesc);
        if(itemCount!=0&&itemDescCount!=0){
            return TaotaoResult.ok();
        }

        return TaotaoResult.build(500,"添加商品有误，请重新输入");
    }

    @Override
    public TaotaoResult downItem(Integer[] ids) {
        int i = tbItemMapper.downItem(ids);
        if (i != 0) {
            return TaotaoResult.ok();
        }
        return null;
    }

    @Override
    public TaotaoResult upItem(Integer[] ids) {
        int i = tbItemMapper.upItem(ids);
        if (i != 0) {
            return TaotaoResult.ok();
        }
        return null;
    }

    @Override
    public TaotaoResult getItemDesc(Long id) {
        TbItemDesc itemDesc = tbItemDescMapper.findDescById(id);
        return TaotaoResult.ok(itemDesc);
    }

    @Override
    public TaotaoResult updateItem(TbItem item, String desc) {
        int i = tbItemMapper.updateItem(item);
        Long ID = item.getId();
        int j = tbItemDescMapper.updateItemDesc(desc,ID);
        TaotaoResult result = new TaotaoResult();
        result.setStatus(200);
        return result;
    }
}
