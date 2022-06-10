package com.klc.usercenter.once;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ImportXingQiuUser {
    public static void main(String[] args){
        String fileName = "D:\\CODE\\klc\\usercenter\\backend\\user-center\\src\\main\\resources\\prodExcel.xlsx";

        List<XingQiuUserInfo> userInfoList = EasyExcel.read(fileName).head(XingQiuUserInfo.class).sheet().doReadSync();

        System.out.println("总数: " + userInfoList.size());

        Map<String, List<XingQiuUserInfo>> listMap = userInfoList.stream().filter(userInfo -> StringUtils.isNotEmpty(userInfo.getUsername())).collect(Collectors.groupingBy(XingQiuUserInfo::getUsername));

        System.out.println("不同昵称总数: " + listMap.size());
    }
}
