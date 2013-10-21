package com.bee.bvo;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-23
 * Time: 上午7:43
 * To change this template use File | Settings | File Templates.
 */
public class WsBvo {
    private String DictNo;
    private String DictName;

    public String getDictNo() {
        return DictNo;
    }

    public void setDictNo(String dictNo) {
        DictNo = dictNo;
    }

    public String getDictName() {
        return DictName;
    }

    public void setDictName(String dictName) {
        DictName = dictName;
    }

    @Override
    public String toString() {
        return "WsBvo{" +
                "DictNo='" + DictNo + '\'' +
                ", DictName='" + DictName + '\'' +
                '}';
    }
}
