package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
    EasyUIResult findContentAll(long contentCategoryId);
    TaotaoResult addContent(TbContent tbContent);

    TaotaoResult updateContent(TbContent tbContent);

    TaotaoResult deleteContent(Integer[] ids);

    void updateCont(int id, String name);

    void deleteCont(Long id);
}
