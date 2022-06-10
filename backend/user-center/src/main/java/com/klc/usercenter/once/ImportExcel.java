package com.klc.usercenter.once;

import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 * 导入excel
 */
public class ImportExcel {

    public static void main(String[] args) {
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1
        String fileName = "D:\\CODE\\klc\\usercenter\\backend\\user-center\\src\\main\\resources\\testExcel.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
        readByListener(fileName);
        synchronousRead(fileName);
    }

    /**
     * 监听器读取
     * @param fileName
     */
    public static void readByListener(String fileName) {
        EasyExcel.read(fileName, XingQiuUserInfo.class, new TableListener()).sheet().doRead();
    }

    /**
     * 同步读取
     * @param fileName
     */
    public static void synchronousRead(String fileName) {
        List<XingQiuUserInfo> totalDataList = EasyExcel.read(fileName).head(XingQiuUserInfo.class).sheet().doReadSync();
        for (XingQiuUserInfo curData : totalDataList) {
            System.out.println(curData);
        }
    }
}
