package com.cj.weixin.gzh;

import java.io.Serializable;

public class GzhEntity implements Serializable{

	/**
	 * 与公众相关的一下参数信息
	 */
	private static final long serialVersionUID = 1L;

	public String tp;
	
	public String token;
	
	public String appid;
	
	public String appsecret;

	public String getTp() {
		return tp;
	}

	public void setTp(String tp) {
		this.tp = tp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}
}
