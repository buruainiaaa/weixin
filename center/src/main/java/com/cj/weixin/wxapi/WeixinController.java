package com.cj.weixin.wxapi;

import com.alibaba.fastjson.JSONObject;
import com.cj.weixin.gzh.GzhService;
import com.cj.weixin.moudle.util.DateUtil;
import com.cj.weixin.moudle.util.WeixinUtil;
import com.cj.weixin.moudle.web.CommonResponse;
import com.cj.weixin.moudle.web.UnitRep;
import com.github.sd4324530.fastweixin.api.JsAPI;
import com.github.sd4324530.fastweixin.api.MenuAPI;
import com.github.sd4324530.fastweixin.api.OauthAPI;
import com.github.sd4324530.fastweixin.api.config.ApiConfig;
import com.github.sd4324530.fastweixin.api.response.GetMenuResponse;
import com.github.sd4324530.fastweixin.api.response.GetSignatureResponse;
import com.github.sd4324530.fastweixin.api.response.OauthGetTokenResponse;
import com.github.sd4324530.fastweixin.ext.GzhInfo;
import com.github.sd4324530.fastweixin.handle.EventHandle;
import com.github.sd4324530.fastweixin.message.BaseMsg;
import com.github.sd4324530.fastweixin.message.TextMsg;
import com.github.sd4324530.fastweixin.message.req.BaseEvent;
import com.github.sd4324530.fastweixin.message.req.TextReqMsg;
import com.github.sd4324530.fastweixin.servlet.WeixinControllerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/weixin")
public class WeixinController extends WeixinControllerSupport {
        private static final Logger log = LoggerFactory.getLogger(WeixinController.class);
        @Autowired
        GzhService gzhService;
        @Autowired
        WXService wxService;
        @Value("${weixin-download-path}")
        private String weixindownloadath;
    	
        /**
         * 设置TOKEN，用于绑定微信服务器
         */
        @Override
        protected GzhInfo getToken(String tp) {
        	GzhInfo info=new GzhInfo();
        	ApiConfig apiConfig =  WeixinUtil.apiMap.get(tp);
        	String token = WeixinUtil.gzhTokenMap.get(tp);
        	info.setToken(token);
        	info.setAppid(apiConfig.getAppid());
            return info;
        }
        
        /**
         * 处理添加关注事件，有需要时子类重写
         *
         * @param event 添加关注事件对象
         * @return 响应消息对象
         */
        @Override
        protected BaseMsg handleSubscribe(BaseEvent event) {
        	log.debug("handleSubscribe:{}", JSONObject.toJSONString(event));
//        	UserAPI userAPI = WeixinUtil.getUserAPIMap(event.getTp());
//        	WXUserEntity ue=this.userService.getUserByOpenid(event.getFromUserName());
//        	if (ue == null) {
//        		 ue=new WXUserEntity();
//        		 ue.setGzhtp(event.getTp());
//        		 GetUserInfoResponse userInfo = userAPI.getUserInfo(event.getFromUserName());
//        		 if (WeixinUtil.isOk(userInfo.getErrcode())) {
//        			try {
//     					BeanUtils.copyProperties(ue, userInfo);
//     				} catch (IllegalAccessException | InvocationTargetException e) {
//     					e.printStackTrace();
//     				}
//             		ue.setCreateTime(DateUtil.getSqlCurrentDate());
//             		if (event instanceof QrCodeEvent) {
//             			QrCodeEvent qrCodeEvent=(QrCodeEvent)event;
//             			if (qrCodeEvent.getEventKey() != null && qrCodeEvent.getEventKey().toLowerCase().startsWith("qrscene_")) {
//             				try {
//             					String sceneId=qrCodeEvent.getEventKey().substring("qrscene_".length());
//    							if (sceneId != null && !sceneId.equals("")) {
//    								ue.setSceneid(Integer.parseInt(sceneId));
//    							}
//							} catch (Exception e) {
//								log.error("parse enventkey failed,due to {}",WxException.getStacktrace(e));
//							}
//						}
//					}
//             		this.userService.saveOrUpdate(ue);
//             		log.info("handleSubscribe:openid={}",event.getFromUserName());
//				}else{
//					log.error(userInfo.getErrmsg());
//				}
//			}
            return new TextMsg("\t 客官里面请，欢迎来到天安金交所。");
        }
        
   
        //重写父类方法，处理对应的微信消息
        @Override
        protected BaseMsg handleTextMsg(TextReqMsg msg) {
        	log.debug("handleTextMsg:{}", JSONObject.toJSONString(msg));
            String content = msg.getContent();
            log.debug("用户发送到服务器的内容:{}", content);
            return new TextMsg("服务器回复用户消息!");
        }
        
	    /*1.1版本新增，重写父类方法，加入自定义微信消息处理器
	     *不是必须的，上面的方法是统一处理所有的文本消息，如果业务觉复杂，上面的会显得比较乱
	     *这个机制就是为了应对这种情况，每个MessageHandle就是一个业务，只处理指定的那部分消息
	     */
 /*     @Override
        protected List<MessageHandle> initMessageHandles() {
                List<MessageHandle> handles = new ArrayList<MessageHandle>();
                handles.add(new MyMessageHandle());
                return handles;
        }*/
      
        
        /**
         * 事件业务分开处理
         */
        @Override
        protected List<EventHandle> initEventHandles() {
            List<EventHandle> handles = new ArrayList<EventHandle>();
            handles.add(new ClickEvent1());
            handles.add(new ClickEvent2());
            return handles;
        }


	@RequestMapping(value="test",method= RequestMethod.GET)
        @ResponseBody
        public String test(HttpServletRequest request){
        	String url=request.getRequestURL().toString()+"?"+request.getQueryString();
        	log.debug("test:{}",url);
        	return url;
        }
        
        /**
         * 通过code获取openid
         * @param code
         * @param tp
         * @return
         */
        @RequestMapping(value="getopenid",method={RequestMethod.GET, RequestMethod.POST},produces = "application/json;charset=UTF-8")
        @ResponseBody
        public ResponseEntity<CommonResponse> getOpenId(@RequestParam String code, @RequestParam String tp){
        	log.debug("getOpenId:code:{},tp:{}", code, tp);
        	OauthAPI oauthAPI = WeixinUtil.getOauthAPI(tp);
        	OauthGetTokenResponse rep=oauthAPI.getToken(code);
        	CommonResponse response=new CommonResponse();
        	if (WeixinUtil.isOk(rep.getErrcode())) {
        		response.with("openid",rep.getOpenid());
			}else{
				response.setErrorCode(Integer.parseInt(rep.getErrcode()));
				response.setErrorMessage(rep.getErrmsg());
				log.error("getopenid:ErrorCode={},ErrorMessage={}",rep.getErrcode(),rep.getErrmsg());
			}
        	log.debug("getOpenId:code:{},tp:{},openid:{}", code, tp, rep.getOpenid());
        	return new ResponseEntity<CommonResponse>(response, HttpStatus.OK);
        }
        
        /**
         * 通过code换取场景id  
         * @param code
         * @param tp
         * @return
         */
        @RequestMapping(value="getsceneid",method={RequestMethod.GET, RequestMethod.POST},produces = "application/json;charset=UTF-8")
        @ResponseBody
        public ResponseEntity<CommonResponse> getSceneId(@RequestParam String code, @RequestParam String tp){
        	log.debug("getSceneId:code:{},tp:{}", code, tp);
        	OauthAPI oauthAPI = WeixinUtil.getOauthAPI(tp);
        	OauthGetTokenResponse rep=oauthAPI.getToken(code);
        	CommonResponse response=new CommonResponse();
        	String sceneid="";
        	if (WeixinUtil.isOk(rep.getErrcode())) {
//        		WXUserEntity wxuser=this.userService.getUserByOpenid(rep.getOpenid());
        		response.with("openid",rep.getOpenid());

//        		if (wxuser != null && wxuser.getSceneid() != null && wxuser.getSceneid() > 0) {
//        			sceneid=Integer.toHexString(wxuser.getSceneid());
//				}
        		response.with("sceneid", sceneid);
			}else{
				response.setErrorCode(Integer.parseInt(rep.getErrcode()));
				response.setErrorMessage(rep.getErrmsg());
				log.error("getopenid:ErrorCode={},ErrorMessage={}",rep.getErrcode(),rep.getErrmsg());
			}
        	log.debug("getSceneId:code:{},tp:{},openid:{},sceneid:{}", code, tp, rep.getOpenid(),sceneid);
        	return new ResponseEntity<CommonResponse>(response, HttpStatus.OK);
        }
        
     
        
        @RequestMapping(value="getjssdk",method={RequestMethod.GET, RequestMethod.POST},produces = "application/json;charset=UTF-8")
        @ResponseBody
        public ResponseEntity<CommonResponse> getJSSdk(@RequestParam String url, @RequestParam String tp,HttpServletRequest request){
        	log.debug("getJSSdk:url:{},tp:{}", url, tp);
        	JsAPI jsAPI=WeixinUtil.getJsAPI(tp);
        	CommonResponse rep=new CommonResponse();
        	GetSignatureResponse srep=jsAPI.getSignature(url);
        	UnitRep ur=new UnitRep();
        	ur.with("noncestr",srep.getNoncestr());
        	ur.with("timestamp",srep.getTimestamp());
        	ur.with("signature",srep.getSignature());
        	ApiConfig apiConfig =  WeixinUtil.apiMap.get(tp);
        	ur.with("appId",apiConfig.getAppid());
        	rep.with(ur);
        	return new ResponseEntity<CommonResponse>(rep, HttpStatus.OK);
        }

        @RequestMapping("get-menu")
        @ResponseBody
        public GetMenuResponse get(String tp){
            MenuAPI menuAPI=WeixinUtil.getMenuAPIMapImpl(tp);
            GetMenuResponse menuResponse=menuAPI.getMenu();
            return menuResponse;
        }

    @RequestMapping("redirect")
    public String get(HttpServletRequest request, HttpServletResponse response){

        return "redirect:http://dev-wx.cgitm.com/app/#/certificate";
    }

    @RequestMapping("save-picture")
    public ResponseEntity<CommonResponse> savePicture(String mediaId,String tp) {
        log.debug("savePicture:mediaId:{},tp:{}", mediaId, tp);
        ApiConfig apiConfig=WeixinUtil.apiMap.get(tp);
        String accessToken=apiConfig.getAccessToken();
        CommonResponse rep=new CommonResponse();
        String filePath = null;
        String filename=null;
        Map<String,String> map=new HashMap<>();
        try {
            filename = SaveImg.saveImageToDisk(mediaId,accessToken,weixindownloadath+ DateUtil.formatDate(new Date())+"\\");
            map=SaveImg.getPictureSize(filename);
        } catch (IOException e) {
            UnitRep ur=new UnitRep();
            ur.with("errMsg",e.getMessage());
            rep.with(ur);
            return new ResponseEntity<CommonResponse>(rep, HttpStatus.NO_CONTENT);
        }


        UnitRep ur=new UnitRep();
        filename=filename.substring(filename.indexOf("\\")+1).replace("weixinDownload","").replace("\\","/");
        ur.with("filename",filename);
        ur.putAll(map);
        rep.with(ur);
        return new ResponseEntity<CommonResponse>(rep, HttpStatus.OK);
    }



}
