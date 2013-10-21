package com.bee.app.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-11-30
 * Time: 上午7:58
 * To change this template use File | Settings | File Templates.
 */
public class BeeSource implements java.io.Serializable{
    private String a1, a2, a4, a5, a6, a7, a8, a9,a10,a11,a12,a13,a14,a15,addTime,editTime,a16;
    private String id;
    private String status;
    
    private String unitNumber;
    
    private String unitName;

    public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unit) {
		this.unitNumber = unit;
	}

	public String getA16() {
        return a16;
    }

    public void setA16(String a16) {
        this.a16 = a16;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {  //数据库中的KEY 键
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getA1() {
        return a1;
    }

    public void setA1(String a1) {
        this.a1 = a1;
    }

    public String getA2() {
        return a2;
    }

    public void setA2(String a2) {
        this.a2 = a2;
    }

    public String getA4() {
        return a4;
    }

    public void setA4(String a4) {
        this.a4 = a4;
    }

    public String getA6() {
        return a6;
    }

    public void setA6(String a6) {
        this.a6 = a6;
    }

    public String getA7() {
        return a7;
    }

    public void setA7(String a7) {
        this.a7 = a7;
    }

    public String getA9() {
        return a9;
    }

    public void setA9(String a9) {
        this.a9 = a9;
    }

    public String getA10() {
        return a10;
    }

    public void setA10(String a10) {
        this.a10 = a10;
    }

    public String getA11() {
        return a11;
    }

    public void setA11(String a11) {
        this.a11 = a11;
    }

    public String getA12() {
        return a12;
    }

    public void setA12(String a12) {
        this.a12 = a12;
    }

    public String getA13() {
        return a13;
    }

    public void setA13(String a13) {
        this.a13 = a13;
    }

    public String getA14() {
        return a14;
    }

    public void setA14(String a14) {
        this.a14 = a14;
    }

    public String getA15() {
        return a15;
    }

    public void setA15(String a15) {
        this.a15 = a15;
    }

    public String getA5() {
        return a5;
    }

    public void setA5(String a5) {
        this.a5 = a5;
    }

    public String getA8() {
        return a8;
    }

    public void setA8(String a8) {
        this.a8 = a8;
    }

    @Override
    public String toString() {
        return "InfoBvo{" +
                "a1='" + a1 + '\'' +
                ", a2='" + a2 + '\'' +
                ", a4='" + a4 + '\'' +
                ", a5='" + a5 + '\'' +
                ", a6='" + a6 + '\'' +
                ", a7='" + a7 + '\'' +
                ", a8='" + a8 + '\'' +
                ", a9='" + a9 + '\'' +
                ", a10='" + a10 + '\'' +
                ", a11='" + a11 + '\'' +
                ", a12='" + a12 + '\'' +
                ", a13='" + a13 + '\'' +
                ", a14='" + a14 + '\'' +
                ", a15='" + a15 + '\'' +
                ", addTime='" + addTime + '\'' +
                ", editTime='" + editTime + '\'' +
                ", a16='" + a16 + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
