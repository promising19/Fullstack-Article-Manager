package com.itheima.interceptors;

import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtil;
import com.itheima.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        //获取token
        String token = request.getHeader("Authorization");
        //解析token
        //验证token
        try {
            // 从redis中获取相同的token
            ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
            // 这里只要传入一个token就行
            // 因为在用户登录部分存储到redis的时候传入的键就是token，值也是token
            // 这样在有多个用户登录后，redis中的token键都不同
            // 这样就相当于找键
            // 如果找不到则说明redis中不存在该令牌
            String redisToken = operations.get(token);
            if (redisToken == null){
                //token已经失效
                throw new RuntimeException();
            }
            Map<String,Object> claims = JwtUtil.parseToken(token);

            //把业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);
            return true; //验证成功-->放行
        } catch (Exception e) {
            //http相应状态码为401
            response.setStatus(401);
            return false; //不放行
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        //清空ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
