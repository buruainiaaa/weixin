package com.cj.weixin.moudle.web;

import com.cj.weixin.moudle.util.StringUtil;

public class Response {
	private int errorCode = 0;
	private String errorMessage = StringUtil.EMPTY;
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
