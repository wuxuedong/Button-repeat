package com.button.demo1.aspect;

import com.alibaba.fastjson.JSON;
import com.button.demo1.annotation.PreventRepeatSubmit;
import com.button.demo1.exception.BusinessException;
import com.button.demo1.utils.HttpCodeEnum;
import com.button.demo1.utils.RedisCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class PreventRepeatSubmitAspect {
    private static final Logger LOG = LoggerFactory.getLogger(PreventRepeatSubmitAspect.class);

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    @Autowired
    private RedisCache redisCache;

    // 定义一个切入点
    @Pointcut("@annotation(com.button.demo1.annotation.PreventRepeatSubmit)")
    public void preventRepeatSubmit() {
    }


    @Around("preventRepeatSubmit()")
    public Object checkPrs(ProceedingJoinPoint pjp) throws Throwable {
        LOG.info("进入preventRepeatSubmit切面");
        //得到request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURI = request.getRequestURI();
        LOG.info("防重复提交的请求地址：{} ,请求方式：{}",requestURI,request.getMethod());
        LOG.info("防重复提交拦截到的类名：{} ,方法：{}",pjp.getTarget().getClass().getSimpleName(),pjp.getSignature().getName());

        //获取请求参数
        Object[] args = pjp.getArgs();
        String argStr = JSON.toJSONString(args);
        //这里替换是为了在redis可视化工具中方便查看
        argStr=argStr.replace(":","#");
        // 唯一值（没有消息头则使用请求地址）
        String submitKey = request.getHeader(header).trim();
        // 唯一标识（指定key + url +参数+token）
        String cacheRepeatKey = "repeat_submit:" + requestURI+":" +argStr+":"+ submitKey;
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method=ms.getMethod();
        PreventRepeatSubmit preventRepeatSubmit=method.getAnnotation(PreventRepeatSubmit.class);
        int interval = preventRepeatSubmit.interval();
        LOG.info("获取到preventRepeatSubmit的有效期时间"+interval);
        //redis分布式锁
        Boolean aBoolean = redisCache.setNxCacheObject(cacheRepeatKey, 1, preventRepeatSubmit.interval(), TimeUnit.SECONDS);
        //aBoolean为true则证明没有重复提交
        if(!aBoolean){
            throw new BusinessException(HttpCodeEnum.REPEATE_ERROR);
        }
        return pjp.proceed();
    }
}
