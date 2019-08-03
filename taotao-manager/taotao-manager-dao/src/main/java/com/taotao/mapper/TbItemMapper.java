package com.taotao.mapper;

import com.taotao.pojo.TbItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TbItemMapper {
    @Select("SELECT * FROM tbitem WHERE id = #{id}")
    TbItem findItemById(Long itemId);
    @Select("SELECT * FROM tbitem")
    List<TbItem> findItemAll();
    @Delete("<script> DELETE FROM tbitem WHERE id IN <foreach collection='array' item='id' open='(' separator=',' close=')'>#{id}</foreach> </script>")
    int deleteItems(Integer[] ids);
    @Insert("INSERT INTO tbitem(id, title, sellPoint, price, num, barcode, image, cid, created, updated) VALUE(#{id},#{title},#{sellPoint},#{price},#{num},#{barcode},#{image},#{cid},#{created},#{updated})")
    int addItems(TbItem tbItem);
    @Update("<script> update tbitem set status = (2) WHERE id IN <foreach collection='array' item='id' open='(' separator=',' close=')'>#{id}</foreach> </script>")
    int downItem(Integer[] ids);
    @Update("<script> update tbitem set status = (1) WHERE id IN <foreach collection='array' item='id' open='(' separator=',' close=')'>#{id}</foreach> </script>")
    int upItem(Integer[] ids);
    @Update("update tbitem set title = #{title},sellPoint = #{sellPoint},price = #{price},num = #{num},barcode = #{barcode},image = #{image},cid = #{cid} where id = #{id}")
    int updateItem(TbItem item);
}