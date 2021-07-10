package com.xunxiang.xunxiangwikibase.common.exception;

import com.xunxiang.xunxiangwikibase.XunxiangwikibaseApplication;
import com.xunxiang.xunxiangwikibase.resp.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(value = RuntimeException.class)
//    public CommonResp handler(RuntimeException e){
//        CommonResp commonResp = new CommonResp();
//        log.error("Error in Operation ------------{}",e);
//        return Result.fail(e.getMessage());
//
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResp handler(MethodArgumentNotValidException e){
        CommonResp commonResp = new CommonResp();
        LOG.error("Error in Field Validation ------------{}",e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        commonResp.setMessage(objectError.getDefaultMessage());
        return commonResp;

    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(value = IllegalArgumentException.class)
//    public Result handler(IllegalArgumentException e){
//        log.error("Error in Assert Argument ------------{}",e);
//        return Result.fail(e.getMessage());
//
//    }

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(value = ShiroException.class)
//    public Result handler(ShiroException e){
//        log.error("Error in Authorization Failure ------------{}",e);
//        return Result.fail(401,e.getMessage(),null);
//    }
}
