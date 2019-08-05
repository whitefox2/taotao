package com.taotao.mapper;


import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

public interface TbContentMapper {
    @Select("SELECT * from tbcontent where categoryId = #{categoryId}")
    List<TbContent> findContentByCategoryId(long contentCategoryId);
    @Insert("INSERT INTO tbcontent(categoryId, title, subTitle, titleDesc, url, pic, pic2, content, created, updated) VALUE (#{categoryId},#{title},#{subTitle},#{titleDesc},#{url},#{pic},#{pic2},#{content},#{created},#{updated})")
    void addContent(TbContent tbContent);
    @Update("UPDATE tbcontent set title = #{title} ,subTitle = #{subTitle},titleDesc = #{titleDesc},url = #{url},pic = #{pic},pic2 = #{pic2},content = #{content},updated = #{updated} where id = #{id}")
    int updateContent(TbContent tbContent);
    @Delete("<script> DELETE FROM tbcontent WHERE id IN <foreach collection='array' item='id' open='(' separator=',' close=')'>#{id}</foreach> </script>")
    int deleteContent(Integer[] ids);
    @Update("update tbcontentcategory set name = #{1} where id = #{0}")
    void updateCont(int id, String name);
    @Select("select * from tbcontentcategory where id = #{id}")
    TbContentCategory findContentById(Long id);
    @Select("SELECT * from tbcontentcategory where parentId = #{id}")
    List<TbContentCategory> findContentByParentId(Long id);
    @Delete("delete from tbcontentcategory where id = #{id}")
    void deleteContentById(Long id);
    @Update("update tbcontentcategory set isParent = #{0} where id = #{id}")
    void updateContentCategory(Long id);
}