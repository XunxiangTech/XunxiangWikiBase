package com.xunxiang.xunxiangwikibase.common.exception;

import com.alibaba.fastjson.JSON;
import com.xunxiang.xunxiangwikibase.XunxiangwikibaseApplication;
import com.xunxiang.xunxiangwikibase.resp.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResp handler(MethodArgumentNotValidException e){
        CommonResp commonResp = new CommonResp();
        LOG.error("Error in Field Validation ------------"+e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        commonResp.setMessage(objectError.getDefaultMessage());
        commonResp.setSuccess(false);
        return commonResp;

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = AuthenticationException.class)
    public CommonResp handler(AuthenticationException e){
        CommonResp commonResp = new CommonResp();
        LOG.error("Error in User Authentication ------------"+e);
        commonResp.setMessage("该用户不存在");
        commonResp.setSuccess(false);
        commonResp.setStatus(400);
        return commonResp;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IncorrectCredentialsException.class)
    public CommonResp handler(IncorrectCredentialsException e){
        CommonResp commonResp = new CommonResp();
        LOG.error("Error in Password Matching ------------"+e);
        commonResp.setMessage("密码错误");
        commonResp.setSuccess(false);
        commonResp.setStatus(400);
        return commonResp;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = LockedAccountException.class)
    public CommonResp handler(LockedAccountException e){
        CommonResp commonResp = new CommonResp();
        LOG.error("Error in Account State ------------"+e);
        commonResp.setMessage("用户已被冻结");
        commonResp.setSuccess(false);
        commonResp.setStatus(400);
        return commonResp;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Exception.class)
    public CommonResp handler(Exception e){
        CommonResp commonResp = new CommonResp();
        LOG.error("Error in System ------------"+e);
        commonResp.setMessage("系统故障");
        commonResp.setSuccess(false);
        commonResp.setStatus(400);
        return commonResp;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = UnauthorizedException.class)//处理访问方法时权限不足问题
    public CommonResp handler(HttpServletResponse res, Exception e) throws IOException {
        LOG.error("Error in Authorization ------------"+e);
//        res.setHeader("Access-Control-Allow-Credentials", "*");
//        res.setContentType("application/json; charset=utf-8");
//        res.setStatus(HttpServletResponse.SC_OK);
//        PrintWriter writer = res.getWriter();
        CommonResp resp = new CommonResp();
        resp.setSuccess(false);
        resp.setMessage("权限不足");
        resp.setStatus(401);
//        Map<String, Object> map= new HashMap<>();
//        map.put("status", 3);
//        map.put("message", "权限不足");
//        writer.write(JSON.toJSONString(resp));
//        writer.close();
        return resp;
    }

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(value = ShiroException.class)
//    public Result handler(ShiroException e){
//        log.error("Error in Authorization Failure ------------{}",e);
//        return Result.fail(401,e.getMessage(),null);
//    }
}
