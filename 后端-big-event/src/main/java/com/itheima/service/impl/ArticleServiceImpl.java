package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.ArticleMapper;
import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.service.ArticleService;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Article article) {
        //注意还要注入时间等元素
        //因此补充属性值
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        //还要拿到当前已经登录的用户id
        //从拦截器中获取
        //拿到业务数据的map集合
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        article.setCreateUser(userId);

        articleMapper.add(article);

    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {

        // 1、创建PageBean对象，用来封装返回的信息
        PageBean<Article> pb = new PageBean<>();

        // 2、开启分页查询 借助PageHelper插件
        PageHelper.startPage(pageNum, pageSize);

        // 3、调用mapper完成查询
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        // 使用 PageHelper 执行查询，直接返回 Page 类型的数据
        List<Article> as = articleMapper.list(userId, categoryId, state);

        // as 已经是 Page 类型了，不需要强制转换
        if (as instanceof Page) {
            // 如果 as 是 Page 类型，我们可以直接用它来获取分页信息
            Page<Article> p = (Page<Article>) as;

            // 把数据填充到 pageBean 对象中
            pb.setTotal(p.getTotal());  // 设置总记录数
            pb.setItems(p.getResult()); // 设置当前页的数据
        } else {
            // 如果返回结果不是 Page 类型，可以通过查询结果的总数手动设置分页信息
            pb.setTotal((long) as.size()); // 总记录数
            pb.setItems(as);        // 当前页的数据
        }

        return pb;
    }

    @Override
    public void update(Article article) {
        // 要知道更新的时间，更新的作者id
        article.setUpdateTime(LocalDateTime.now());
        // 再调用mapper层的更新方法
        articleMapper.update(article);
    }

    @Override
    public Article findById(Integer id) {
        Article a = articleMapper.findById(id);
        return a;
    }

    @Override
    public void delete(Integer id) {
        articleMapper.delete(id);
    }

}
