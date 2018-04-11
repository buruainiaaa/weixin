package com.cj.weixin.wxapi;

import com.alibaba.fastjson.JSONObject;
import com.github.sd4324530.fastweixin.handle.EventHandle;
import com.github.sd4324530.fastweixin.message.BaseMsg;
import com.github.sd4324530.fastweixin.message.TextMsg;
import com.github.sd4324530.fastweixin.message.req.BaseEvent;
import com.github.sd4324530.fastweixin.message.req.EventType;
import com.github.sd4324530.fastweixin.message.req.MenuEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClickEvent1 implements EventHandle<BaseEvent>{
	  private static final Logger log = LoggerFactory.getLogger(ClickEvent1.class);
	private static final String menuid="sub3";

	@Override
	public BaseMsg handle(BaseEvent event) {
		return new TextMsg("服务时间：9:00-18:00 （工作日）\n客服电话：4000-888-999\n联系地址：上海市浦东新区世纪大道东方 B座36F");
	}

	@Override
	public boolean beforeHandle(BaseEvent event) {
		log.debug(JSONObject.toJSONString(event));
		if (event.getEvent().equals(EventType.CLICK) && event instanceof MenuEvent) {
			MenuEvent newEvent=(MenuEvent)event;
			return menuid.equals(newEvent.getEventKey());
		}
		return false;
	}

}
