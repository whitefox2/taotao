package com.taotao.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;

public interface ItemParamService {
    TaotaoResult getItemParamByCid(Long itemCatId);

    TaotaoResult addItemParam(TbItemParam tbItemParam);
}
