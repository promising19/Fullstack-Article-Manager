package com.itheima.controller;

import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    //导入service对象
    @Autowired
    private ArticleService articleService;

    //编写一个方法实现文章的新增
    @PostMapping
    public Result add(@RequestBody @Validated Article article){
        articleService.add(article);
        return Result.success();
    }

    //编写列出文章的方法
    //返回的参数是一个Page Bean对象
    @GetMapping
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String state
    ){
        //方法内直接调用service层的list方法
        PageBean<Article> pb = articleService.list(pageNum,pageSize,categoryId,state);
        return Result.success(pb);
    }

    //编写更新文章的方法
    @PutMapping
    public Result update(@RequestBody Article article){
        // 在这个方法中调用service层的更新方法
        articleService.update(article);
        return Result.success();
    }

    //编写获取文章详情的方法
    // 返回的是文章信息类型应该是article
    // 传入的参数应该为文章的id
    @GetMapping("/detail")
    public Result<Article> detail(Integer id){
        // 在方法中调用service层的方法
        // 需要接收返回的内容并返回结果
        Article a = articleService.findById(id);
        return Result.success(a);
    }

    // 编写删除文章的方法
    @DeleteMapping
    public Result delete(Integer id){
        articleService.delete(id);
        return Result.success();
    }

}
