package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUIResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;
    @Override
    public EasyUIResult findContentAll(long contentCategoryId) {
        List<TbContent> contents = tbContentMapper.findContentByCategoryId(contentCategoryId);
        EasyUIResult result = new EasyUIResult();
        result.setTotal(contents.size());
        result.setRows(contents);
        return result;
    }

    @Override
    public TaotaoResult addContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        tbContentMapper.addContent(tbContent);
        return TaotaoResult.ok(tbContent);
    }

    @Override
    public TaotaoResult updateContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setUpdated(date);
        int i = tbContentMapper.updateContent(tbContent);
        if(i!=0){
            return TaotaoResult.ok(tbContent);
        }
        return null;
    }

    @Override
    public TaotaoResult deleteContent(Integer[] ids) {
        int i = tbContentMapper.deleteContent(ids);
        if(i!=0){
            return TaotaoResult.ok();
        }
        return null;
    }

    @Override
    public void updateCont(int id, String name) {
        tbContentMapper.updateCont(id,name);
    }

    @Override
    public void deleteCont(Long id) {
        TbContentCategory tbContentCategory = tbContentMapper.findContentById(id);
        if (tbContentCategory.getIsParent()) {
            List<TbContentCategory> tbContentCategories = tbContentMapper.findContentByParentId(id);
            for (TbContentCategory tb : tbContentCategories) {
                tbContentMapper.deleteContentById(tb.getId());
            }
        }
            tbContentMapper.deleteContentById(id);
    }
}
