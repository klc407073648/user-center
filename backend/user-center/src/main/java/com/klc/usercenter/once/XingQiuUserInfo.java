package com.klc.usercenter.once;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 星球表用户信息
 */
@Data
public class XingQiuUserInfo {

    @ExcelProperty("成员昵称")
    private String username;

    @ExcelProperty("成员编号")
    private String planetCode;
}