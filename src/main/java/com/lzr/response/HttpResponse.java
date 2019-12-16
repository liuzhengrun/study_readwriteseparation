package com.lzr.response;

import com.lzr.exception.CustomizeException;
import com.lzr.response.enums.ResponseResultEnums;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lzr
 * @date 2019/3/27 0027 14:28
 */
@Data
@AllArgsConstructor
public class HttpResponse implements Serializable {

    private String code;

    private String msg;

    private Object data;

    public static HttpResponse success() {
        return success(null);
    }

    public static HttpResponse success(Object data) {
        return success(ResponseResultEnums.SUCCESS.getCode(), ResponseResultEnums.SUCCESS.getMsg(), data);
    }

    public static HttpResponse success(String code, String msg) {
        return success(code, msg, null);
    }

    public static HttpResponse success(String msg, Object data) {
        return success(ResponseResultEnums.SUCCESS.getCode(), msg, data);
    }

    public static HttpResponse success(String code, String msg, Object data) {
        return new HttpResponse(code, msg, data);
    }

    public static HttpResponse fail(String code, String msg) {
        throw new CustomizeException(code,msg);
    }

    public static HttpResponse fail(String msg){
        throw new CustomizeException(ResponseResultEnums.FAIL.getCode(), msg);
    }

    public static HttpResponse fail(ResponseResultEnums responseResultEnums){
        throw new CustomizeException(responseResultEnums.getCode(),responseResultEnums.getMsg());
    }

    public static HttpResponse fail(){
        throw new CustomizeException(ResponseResultEnums.FAIL);
    }

    /**
     * 只用于过滤器 返回数据
     */
    public static HttpResponse filterData(ResponseResultEnums responseResultEnums){
        return HttpResponse.success(responseResultEnums.getCode(),responseResultEnums.getMsg());
    }

}
