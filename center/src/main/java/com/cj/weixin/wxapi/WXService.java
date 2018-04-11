package com.cj.weixin.wxapi;

import com.github.sd4324530.fastweixin.api.CustomAPI;
import com.github.sd4324530.fastweixin.api.OauthAPI;
import com.github.sd4324530.fastweixin.api.QrcodeAPI;
import com.github.sd4324530.fastweixin.api.enums.QrcodeType;
import com.github.sd4324530.fastweixin.api.response.OauthGetTokenResponse;
import com.github.sd4324530.fastweixin.api.response.QrcodeResponse;
import com.github.sd4324530.fastweixin.message.BaseMsg;
import com.cj.weixin.moudle.util.StringUtil;
import com.cj.weixin.moudle.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WXService {
	 private static final Logger log = LoggerFactory.getLogger(WXService.class);
	/**
	 * 通过code换取openid
	 * @param code
	 * @param tp
	 * @return
	 */
	public String getOpenid(String code,String tp){
		OauthAPI oauthAPI = WeixinUtil.getOauthAPI(tp);
    	OauthGetTokenResponse rep=oauthAPI.getToken(code);
    	if (WeixinUtil.isOk(rep.getErrcode())) {
    		return rep.getOpenid();
		}else{
			return null;
		}
	}
	
	/**
	 * 给指定openid发送消息
	 * @param openid
	 * @param tp
	 * @param message
	 */
	public void sendMsg(String openid,String tp,BaseMsg message){
		CustomAPI api=WeixinUtil.getCustomAPIMap(tp);
		if (!StringUtil.isEmpty(openid)) {
			api.sendCustomMessage(openid, message);
		}
	}
	
	/**
	 * 获取指定场景编号的ticket，用来生成二维码
	 * @param sceneId
	 * @param tp
	 * @return
	 */
	public String getQrCodeTicket(String sceneId,String tp){
		QrcodeAPI api= WeixinUtil.getQrcodeAPIMap(tp);
		QrcodeResponse rep =api.createQrcode(QrcodeType.QR_SCENE, sceneId, 2592000);
		if (WeixinUtil.isOk(rep.getErrcode())) {
			return rep.getTicket();
		}else{
			log.error("getQrCodeTicket:errcode={},errmsg={}",rep.getErrcode(),rep.getErrmsg());
		}
		return null;
	}
}
