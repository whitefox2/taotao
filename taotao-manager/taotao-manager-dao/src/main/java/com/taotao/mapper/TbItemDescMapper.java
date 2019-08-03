package com.taotao.mapper;

import com.taotao.pojo.TbItemDesc;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface TbItemDescMapper {
    @Insert("INSERT INTO tbitemdesc(itemId, itemDesc, created, updated) VALUE (#{itemId},#{itemDesc},#{created},#{updated})")
    int addItemDesc(TbItemDesc desc);
    @Select("SELECT * FROM tbitemdesc WHERE itemId = #{itemId}")
    TbItemDesc findDescById(Long id);
    @Update("update tbitemdesc set itemDesc = #{0} where itemId = #{1}")
    int updateItemDesc(String desc,Long ID);
}