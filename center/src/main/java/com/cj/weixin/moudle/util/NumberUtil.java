package com.cj.weixin.moudle.util;

import java.text.NumberFormat;
import java.util.Random;

public final class NumberUtil {

	private static NumberFormat formater = NumberFormat.getInstance();

	static {
		formater.setGroupingUsed(false);
		formater.setMaximumFractionDigits(32);
		formater.setMaximumIntegerDigits(32);
	}

	public static String valueOf(double d) {
		return formater.format(d);
	}

	public static String valueOf(int i) {
		return formater.format(i);
	}

	public static String valueOf(long l) {
		return formater.format(l);
	}
	
	public static String randomNumb(){
		Random r=new Random();
		String result="";
		for (int i = 0; i < 6; i++) {
			result +=r.nextInt(10);
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(randomNumb());
	}

}
