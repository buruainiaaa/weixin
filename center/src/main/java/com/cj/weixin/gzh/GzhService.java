package com.cj.weixin.gzh;

import com.github.sd4324530.fastweixin.api.config.ApiConfig;
import com.cj.weixin.moudle.util.JsonUtil;
import com.cj.weixin.moudle.util.WeixinUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class GzhService {
	
	@Value("${chujiu.gzhs}")
	private String gzhJsonArr;
	
	/**
	 * 初始公众号信息到缓存中
	 */
	@PostConstruct
	public void init(){
		//新建公众号ApiConfig
		if(WeixinUtil.gzhTokenMap.size() == 0){
			List<GzhEntity> gzhList = JsonUtil.getGzhs(gzhJsonArr);
			for (GzhEntity gzhEntity : gzhList) {
				WeixinUtil.apiMap.put(gzhEntity.getTp(), new ApiConfig(gzhEntity.getAppid(), gzhEntity.getAppsecret(), true));
				WeixinUtil.gzhTokenMap.put(gzhEntity.getTp(), gzhEntity.getToken());
			}	
		}	
	}

}
