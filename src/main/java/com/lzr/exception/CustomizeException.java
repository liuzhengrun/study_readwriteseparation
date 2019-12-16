package com.lzr.exception;

import com.lzr.response.enums.ResponseResultEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzr
 * @date 2019/5/13 0013 17:01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomizeException extends RuntimeException {

    private String code;

    public CustomizeException(String message) {
        super(message);
    }

    public CustomizeException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CustomizeException(ResponseResultEnums responseResultEnums){
        super(responseResultEnums.getMsg());
        this.code = responseResultEnums.getCode();
    }

}
