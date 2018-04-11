package com.cj.weixin.moudle.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author cody
 * 2018年3月4日下午5:14:58
 */
public class DateUtil {
	public static Timestamp getSqlCurrentDate(){
		return new Timestamp(Clock.DEFAULT.getCurrentTimeInMillis());
	}

	/**
	 * @param date 时间
	 * @return yyyyMMdd格式的时间
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String result = simpleDateFormat.format(date);
		return result;
	}
	
	public static void main(String[] args) {
		String s="D:\\weixinDownload\\image\\20180404\\image.jpg";
		System.out.println(s);
	}

}
