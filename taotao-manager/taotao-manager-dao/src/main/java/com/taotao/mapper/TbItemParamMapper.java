package com.taotao.mapper;


import com.taotao.pojo.TbItemParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface TbItemParamMapper {
    @Select("SELECT * FROM tbitemparam WHERE itemCatId = #{itemCatId}")
    TbItemParam findItemParamByCatId(Long itemCatId);
    @Insert("INSERT INTO tbitemparam(itemCatId, paramData, created, updated) VALUE (#{itemCatId},#{paramData},#{created},#{updated})")
    void addItemParam(TbItemParam tbItemParam);
}