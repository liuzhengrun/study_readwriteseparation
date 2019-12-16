package com.lzr.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lzr
 * @date 2019/12/13 0013 11:26
 */
@Data
@ToString
public class SysUserVO implements Serializable {

    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 性别
     */
    @NotNull(message = "性别不能为空")
    @NotBlank(message = "性别不能为空")
    @Max(value = 1,message = "最大为1(女)")
    @Min(value = 0,message = "最小为0(男)")
    private Integer sex;

    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    @NotBlank(message = "年龄不能为空")
    @Min(value = 0,message = "最小为0")
    private Integer age;

}
