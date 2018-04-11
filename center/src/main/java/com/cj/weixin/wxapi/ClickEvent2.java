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

public class ClickEvent2 implements EventHandle<BaseEvent>{
	  private static final Logger log = LoggerFactory.getLogger(ClickEvent2.class);
	private static final String menuid="sub4";

	@Override
	public BaseMsg handle(BaseEvent event) {
		return new TextMsg("如果贵公司与初九数据优势互补，并有合作意向，请简要描述合作需求发送邮件至tangwj@chujiu-data.com 工作人员会尽快与您联系");
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
