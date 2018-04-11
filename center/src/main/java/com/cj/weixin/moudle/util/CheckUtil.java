package com.cj.weixin.moudle.util;

import com.cj.weixin.moudle.exception.WxException;

/**
 * check util
 * @author cody
 * 2018年3月21日下午1:53:46
 */
public class CheckUtil {
	public static void checkParam(Object param){
		if (param == null || "".equals(param.toString().trim())) {
			throw WxException.getException(10000);
		}
	}
	
	public static void positiveNumb(Object param){
		if (param == null || "".equals(param) || Double.valueOf(param.toString()) <=0) {
			throw WxException.getException(10000);
		}
	}
	
	
	public static void checkEnum(Object[] enums, Object e){
		boolean valid=false;
		for (Object i : enums) {
			if (i.toString().equals(e.toString())) {
				valid=true;
				break;
			}
		}
		if (!valid) {
			throw WxException.getException(10000);
		}
	}
}
