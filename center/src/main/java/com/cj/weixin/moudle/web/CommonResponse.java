package com.cj.weixin.moudle.web;

import com.alibaba.fastjson.JSONObject;
import com.cj.weixin.moudle.exception.WxException;
import com.cj.weixin.moudle.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class CommonResponse {

	public CommonResponse() {
		super();
	}

	public CommonResponse(int errorCode) {
		super();
		this.withError(WxException.getException(errorCode));
	}

	public final static int VALID = 0;

	private int errorCode = 0;
	private String errorMessage = StringUtil.EMPTY;

	private Map<String, Object> body = new HashMap<String, Object>();

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

	public CommonResponse withError(WxException error) {
		this.errorCode = error.getWfdErrorCode();
		this.errorMessage = error.getWfdErrorMsg();
		return this;
	}

	public CommonResponse withError(Throwable error) {
		this.errorCode = -1;
		this.errorMessage = error.getMessage();
		return this;
	}

	public CommonResponse withAttribute(String key, Object value) {
		this.body.put(key, value);
		return this;
	}

	public CommonResponse with(String key, Object value) {
		return this.withAttribute(key, value);
	}

	public CommonResponse withAttributes(Map<String, Object> attrs) {
		this.body.putAll(attrs);
		return this;
	}

	public CommonResponse with(Map<String, Object> attrs) {
		return this.withAttributes(attrs);
	}

	public void addAttribute(String key, Object value) {
		this.body.put(key, value);
	}

	public void addAttributes(Map<String, Object> attrs) {
		this.body.putAll(attrs);
	}

	public Object getAttribute(String key) {
		return this.body.get(key);
	}

	public Map<String, Object> getBody() {
		return body;
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

	public boolean valid() {
		return this.errorCode == VALID;
	}

	public boolean invalid() {
		return !valid();
	}

	@Override
	public String toString() {
		return JSONObject.toJSON(this).toString();
	}

}
