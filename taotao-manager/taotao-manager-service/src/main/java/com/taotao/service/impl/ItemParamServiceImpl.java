package com.taotao.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemParam;
import com.taotao.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ItemParamServiceImpl implements ItemParamService {
    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    @Override
    public TaotaoResult getItemParamByCid(Long itemCatId) {
        TbItemParam tbItemParam = tbItemParamMapper.findItemParamByCatId(itemCatId);
        if(tbItemParam!=null){
            return TaotaoResult.ok(tbItemParam);
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult addItemParam(TbItemParam tbItemParam) {
        //默认页面已经吧要想要的参数 根据 我们的pojo 的属性已经绑定好了不用java程序员去管理
        Date time = new Date();
        tbItemParam.setCreated(time);
        tbItemParam.setUpdated(time);

        tbItemParamMapper.addItemParam(tbItemParam);

        return TaotaoResult.ok();
    }
}
