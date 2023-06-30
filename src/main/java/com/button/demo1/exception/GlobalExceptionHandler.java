package com.button.demo1.exception;

import com.button.demo1.utils.HttpCodeEnum;
import com.button.demo1.utils.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private HttpServletRequest httpServletRequest;

    private final String sysError="系统出错";

    /**
     * 缺少请求体异常处理器
     * @param e 缺少请求体异常 使用get方式请求 而实体使用@RequestBody修饰
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseResult parameterBodyMissingExceptionHandler(HttpMessageNotReadableException e) {
        String requestURI = httpServletRequest.getRequestURI();
        log.error("请求地址'{}',请求体缺失'{}'", requestURI, e.getMessage());
        return ResponseResult.errorResult(HttpCodeEnum.SYSTEM_ERROR.getCode(), sysError);
    }

    /*
     * @Description:  捕获请求方法异常，比如post接口使用了get
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult methodNotAllowedHandler(HttpRequestMethodNotSupportedException e) {
        String requestURI = httpServletRequest.getRequestURI();
        log.error("请求地址'{}',请求方法不被允许'{}'", requestURI, e.getMessage());
        return ResponseResult.errorResult(HttpCodeEnum.SYSTEM_ERROR.getCode(), sysError);
    }

    // get请求的对象参数校验异常
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler({MissingServletRequestParameterException.class})
//    public ResponseResult bindExceptionHandler(MissingServletRequestParameterException e) {
//            String requestURI = httpServletRequest.getRequestURI();
//        log.error("请求地址'{}',get方式请求参数'{}'必传", requestURI, e.getParameterName());
//            return ResponseResult.errorResult(HttpCodeEnum.SYSTEM_ERROR.getCode(), sysError);
//    }
    // post请求的对象参数校验异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseResult methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
        String requestURI = httpServletRequest.getRequestURI();
        log.error("请求地址'{}',post方式请求参数异常'{}'", requestURI, e.getMessage());
            List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
            return ResponseResult.errorResult(HttpCodeEnum.SYSTEM_ERROR.getCode(), getValidExceptionMsg(allErrors));
    }

    // 业务类异常
    @ExceptionHandler(BusinessException.class)
    public ResponseResult businessExceptionHandler(BusinessException e) {
        String requestURI = httpServletRequest.getRequestURI();
        log.error("请求地址'{}',捕获业务类异常'{}'", requestURI, e);
        return ResponseResult.errorResult(HttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }
    // 运行时异常
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult runtimeExceptionHandler(RuntimeException e) {
        String requestURI = httpServletRequest.getRequestURI();
        log.error("请求地址'{}',捕获运行时异常'{}'", requestURI, e);
        return ResponseResult.errorResult(HttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }
    // 系统级别异常
      @ExceptionHandler(Throwable.class)
    public ResponseResult throwableExceptionHandler(Throwable e) {
        String requestURI = httpServletRequest.getRequestURI();
        log.error("请求地址'{}',捕获系统级别异常'{}'", requestURI, e);
        return ResponseResult.errorResult(HttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }



    private String getValidExceptionMsg(List<ObjectError> errors) {
        if(!CollectionUtils.isEmpty(errors)){
            StringBuilder sb = new StringBuilder();
            errors.forEach(error -> {
                if (error instanceof FieldError) {
                    sb.append(((FieldError)error).getField()).append(":");
                }
                sb.append(error.getDefaultMessage()).append(";");
            });
            String msg = sb.toString();
            msg = msg.substring(0, msg.length() -1);
            return msg;
        }
        return null;
    }

}
