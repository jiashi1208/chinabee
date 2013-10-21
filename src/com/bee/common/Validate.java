package com.bee.common;

import android.content.Context;

public class Validate {
	
	public static boolean validateInput(Context context){
		
		/*String _a1 = context.a1.getText().toString().trim();
        if (_a1.equals("")) {
            showDialog("蜜源追溯码不能为空");
        } else if (!_a1.matches("[0-9]+") || Integer.valueOf(_a1) < 100000000 || Integer.valueOf(_a1) > 999999999) {
            showDialog("蜜源追溯码必须为9位数字");
        } else if (a9.getText().toString().trim().equals("")) {
            showDialog("浓度不能为空");
        } else if (Float.parseFloat(a9.getText().toString()) > 1000) {
            showDialog("浓度不能大于等于1000");
        } else if (a2.getText().toString().trim().equals("")) {
            showDialog("饲养人不能为空");
        } else if (a4.getText().toString().trim().equals("")) {
            showDialog("地点不能为空");
        } else if (_a5.trim().equals("")) {
            showDialog("原料种类不能为空");
        } else if (a7.getText().toString().trim().equals("")) {
            showDialog("数量不能为空");
        } else if (a6.getText().toString().trim().equals("")) {
            showDialog("饲料不能为空");
        } else if (_a8.trim().equals("")) {
            showDialog("规格不能为空");
        } else if (a10.getText().toString().trim().equals("")) {
            showDialog("采蜜间隔不能为空");
        } else if (a11.getText().toString().trim().equals("")) {
            showDialog("用药情况不能为空");
        } else if (a12.getText().toString().trim().equals("")) {
            showDialog("首次采收日期不能为空");
        } else if (a13.getText().toString().trim().equals("")) {
            showDialog("末次采收日期不能为空");
        } else if (a14.getText().toString().trim().equals("")) {
            showDialog("收购日期不能为空");
        } else if (a15.getText().toString().trim().equals("")) {
            showDialog("收购单位不能为空");
        } else if (_a16.trim().equals("")) {
            showDialog("蜂种不能为空");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = sdf.parse(a12.getText().toString().trim());
                Date date2 = sdf.parse(a13.getText().toString().trim());
                Date date3 = sdf.parse(a14.getText().toString().trim());
                Date date0 = new Date();
                if (date1.getTime() > date0.getTime()) {
                    showDialog("首次采收日期不能大于当前日期");
                    return;
                }
                if (date2.getTime() > date0.getTime()) {
                    showDialog("末次采收日期不能大于当前日期");
                    return;
                }
                if (date3.getTime() > date0.getTime()) {
                    showDialog("收购日期不能大于当前日期");
                    return;
                }
                if (date1.getTime() > date2.getTime()) {
                    showDialog("首次采收日期不能大于末次采收日期");
                    return;
                }
                if (date2.getTime() > date3.getTime()) {
                    showDialog("末次采收日期不能大于收购日期");
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
*/
		
		
		return true;
	}
	
	public static boolean netCheck(Context context){
		
		return true;
		
	}
	
	public static boolean PhoneNO(Context context){
		return true;
	}

}
