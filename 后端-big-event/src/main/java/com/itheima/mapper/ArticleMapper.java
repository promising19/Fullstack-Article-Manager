package com.itheima.mapper;

import com.itheima.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    //新增
    @Insert("insert into article(title,content,cover_img,state,category_id,create_user,create_time,update_time)" +
            "values(#{title},#{content},#{coverImg},#{state},#{categoryId},#{createUser},#{createTime},#{updateTime})")
    void add(Article article);

    //使用动态查询
    List<Article> list(Integer userId, Integer categoryId, String state);

    //更新
    @Update("UPDATE article SET title = #{title}, content = #{content}, cover_img = #{coverImg}, state = #{state}, category_id = #{categoryId} WHERE id = #{id}")
    void update(Article article);

    // 获取详细信息
    @Select("select * from article where id=#{id}")
    Article findById(Integer id);

    // 删除文章
    @Delete("delete from article where id=#{id}")
    void delete(Integer id);
}
