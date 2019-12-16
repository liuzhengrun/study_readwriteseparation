package com.lzr.response.enums;

/**
 * @author lzr
 * @date 2019/3/27 0027 14:48
 */
public enum ResponseResultEnums {

    ERROR("-9","系统错误"),
    SUCCESS("1","请求成功"),
    FAIL("-1","请求失败"),
    TOKEN_NOT_EXIST("-2","token为空"),
    TOKEN_CERTIFICATION_FAIL("-3","token认证失败"),
    ACCOUNT_SUSPEND("-4","账号被封停"),
    EXCEL_ABNORMAL("-5","excel操作异常"),
    AUTHORIZATION_CERTIFICATION_FAIL("-6","Authorization认证失败"),
    NO_LOGIN_ERROR("-7","未登录,无法访问"),
    NO_PERMISSION("-8","没有权限");

    private String code;// code码
    private String msg;// 消息

    ResponseResultEnums(String code, String msg){this.code=code;this.msg=msg;}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
