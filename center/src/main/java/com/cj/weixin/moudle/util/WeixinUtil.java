package com.cj.weixin.moudle.util;

import com.cj.weixin.wxapi.ApiMenuImpl;
import com.github.sd4324530.fastweixin.api.*;
import com.github.sd4324530.fastweixin.api.config.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WeixinUtil {
	private WeixinUtil(){};
	public static final String OK = "0";
	/**公众号属性*/
	public static Map<String, ApiConfig> apiMap = new HashMap<String, ApiConfig>();
	//存放tp-token
	public static Map<String, String> gzhTokenMap = new HashMap<String, String>();
	static Logger log= LoggerFactory.getLogger(WeixinUtil.class);
	/**管理Api,避免多次创建新实体对象*/
	private static Map<String, OauthAPI> oauthAPIMap=new HashMap<String, OauthAPI>();
	private static Map<String, JsAPI> jsAPIMap=new HashMap<String, JsAPI>();
	private static Map<String, MenuAPI> menuAPIMap=new HashMap<String, MenuAPI>();
	private static Map<String, UserAPI> userAPIMap=new HashMap<String, UserAPI>();
	private static Map<String, CustomAPI> customAPIMap=new HashMap<String, CustomAPI>();
	private static Map<String, QrcodeAPI> qrcodeAPIMap=new HashMap<String, QrcodeAPI>();
	private static Map<String, ApiMenuImpl> apiMenuImplMap=new HashMap<String, ApiMenuImpl>() ;
	/**管理Api,避免多次创建新实体对象*/
	/**
	 * 微信返回结果是否正常
	 * 
	 * @param errcode
	 * @return
	 */
	public static boolean isOk(String errcode) {
		return OK.equals(errcode) || errcode == null;
	}
	
    /**
     * 获取公众号对应的ApiConfig对象
     * @param tp
     * @return
     */
    private static ApiConfig getApiConfig(String tp){
    	ApiConfig config =null;
    	config=WeixinUtil.apiMap.get(tp);
    	if (config == null) {
			log.error("getApiConfig: tp {} is null",tp);
		}
    	return config;
    }
    
    public static OauthAPI getOauthAPI(String tp){
    	OauthAPI api=WeixinUtil.oauthAPIMap.get(tp);
    	if (api == null) {
			api=new OauthAPI(WeixinUtil.getApiConfig(tp));
			WeixinUtil.oauthAPIMap.put(tp, api);
		}
    	return api;
    }
    
    public static JsAPI getJsAPI(String tp){
    	JsAPI api=WeixinUtil.jsAPIMap.get(tp);
    	if (api == null) {
			api=new JsAPI(WeixinUtil.getApiConfig(tp));
			WeixinUtil.jsAPIMap.put(tp, api);
		}
    	return api;
    }
    
    public static MenuAPI getMenuAPIMap(String tp){
    	MenuAPI api=WeixinUtil.menuAPIMap.get(tp);
    	if (api == null) {
			api=new MenuAPI(WeixinUtil.getApiConfig(tp));
			WeixinUtil.menuAPIMap.put(tp, api);
		}
    	return api;
    }

	public static ApiMenuImpl getMenuAPIMapImpl(String tp){
		ApiMenuImpl apiMenuImpl=WeixinUtil.apiMenuImplMap.get(tp);
		if (apiMenuImpl == null) {
			apiMenuImpl=new ApiMenuImpl(WeixinUtil.getApiConfig(tp));
			WeixinUtil.apiMenuImplMap.put(tp, apiMenuImpl);
		}
		return apiMenuImpl;
	}
    
    public static UserAPI getUserAPIMap(String tp){
    	UserAPI api=WeixinUtil.userAPIMap.get(tp);
    	if (api == null) {
			api=new UserAPI(WeixinUtil.getApiConfig(tp));
			WeixinUtil.userAPIMap.put(tp, api);
		}
    	return api;
    }
    
    public static CustomAPI getCustomAPIMap(String tp){
    	CustomAPI api=WeixinUtil.customAPIMap.get(tp);
    	if (api == null) {
			api=new CustomAPI(WeixinUtil.getApiConfig(tp));
			WeixinUtil.customAPIMap.put(tp, api);
		}
    	return api;
    }
    
    public static QrcodeAPI getQrcodeAPIMap(String tp){
    	QrcodeAPI api=WeixinUtil.qrcodeAPIMap.get(tp);
    	if (api == null) {
			api=new QrcodeAPI(WeixinUtil.getApiConfig(tp));
			WeixinUtil.qrcodeAPIMap.put(tp, api);
		}
    	return api;
    }
}
