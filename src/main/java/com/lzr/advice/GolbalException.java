package com.lzr.advice;

import com.lzr.exception.CustomizeException;
import com.lzr.response.HttpResponse;
import com.lzr.response.enums.ResponseResultEnums;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.Calendar;

/**
 * 全局异常判断
 */
@ControllerAdvice
public class GolbalException {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public HttpResponse handleExceptions(HttpServletRequest request, HttpServletResponse response, Exception e){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset = utf-8");
        Calendar calendar= Calendar.getInstance();
        String message = "";
        String code = ResponseResultEnums.ERROR.getCode();
        if (e instanceof NullPointerException){// 空指针异常
            message = "空指针异常";
        }else if(e instanceof HttpMediaTypeNotSupportedException){// 请求类型出错
            message = "不支持的内容格式出错";
        }else if (e instanceof MethodArgumentNotValidException){//swagger2注解判断抛出的异常
            MethodArgumentNotValidException me = (MethodArgumentNotValidException)e;
            message=me.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        }else if (e instanceof IllegalArgumentException){
            IllegalArgumentException ie = (IllegalArgumentException) e;
            message = ie.getMessage();
        }else if (e instanceof SqlSessionException){// 数据库操作出错
            SqlSessionException se = (SqlSessionException)e;
            message = se.getMessage();
        }else if (e instanceof HttpRequestMethodNotSupportedException){
            HttpRequestMethodNotSupportedException he = (HttpRequestMethodNotSupportedException)e;
            message = he.getMessage();
        } else if (e instanceof ConstraintViolationException){// notnull等注解会出现的异常
            ConstraintViolationException me = (ConstraintViolationException)e;
            message=me.getMessage();
        }else if (e instanceof CannotCreateTransactionException){// 数据库连接失败(主要是事务连接)
            CannotCreateTransactionException ce = (CannotCreateTransactionException)e;
            message = "数据库连接失败";
        }else if (e instanceof CustomizeException){
            CustomizeException ce = (CustomizeException)e;
            code = ce.getCode();
            message = ce.getMessage();
        }else {
            message = "发生全局异常";
        }
        System.out.println(calendar.getTime()+" : "+this.getClass().getName()+" : "+message);
        e.printStackTrace();
        return HttpResponse.success(code,message);
    }

}
