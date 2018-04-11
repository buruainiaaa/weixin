package com.cj.weixin.wxapi;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.github.sd4324530.fastweixin.api.MenuAPI;
import com.github.sd4324530.fastweixin.api.config.ApiConfig;
import com.github.sd4324530.fastweixin.api.response.BaseResponse;
import com.github.sd4324530.fastweixin.api.response.GetMenuResponse;
import com.github.sd4324530.fastweixin.util.CollectionUtil;
import com.github.sd4324530.fastweixin.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author cody
 * @create 2018-03-20 9:22
 **/

public class ApiMenuImpl extends MenuAPI {
    private static final Logger LOG = LoggerFactory.getLogger(ApiMenuImpl.class);
    public ApiMenuImpl(ApiConfig config) {
        super(config);
    }

    @Override
    public GetMenuResponse getMenu() {
        GetMenuResponse response = null;
        LOG.debug("获取菜单信息.....");
        String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=#";
        BaseResponse r = this.executeGet(url);
        if (this.isSuccess(r.getErrcode())) {
            JSONObject jsonObject = JSONUtil.getJSONFromString(r.getErrmsg());
            List buttonList = (List) JSONPath.eval(jsonObject, "$.menu.button");
            if (CollectionUtil.isNotEmpty(buttonList)) {
                Iterator var6 = buttonList.iterator();

                label33:
                while(true) {
                    while(true) {
                        if (!var6.hasNext()) {
                            break label33;
                        }

                        Object button = var6.next();
                        List subList = (List)JSONPath.eval(button, "$.sub_button");
                        if (CollectionUtil.isNotEmpty(subList)) {
                            Iterator var12 = subList.iterator();

                            while(var12.hasNext()) {
                                Object sub = var12.next();
                                Object type = JSONPath.eval(sub, "$.type");
                                JSONPath.set(sub, "$.type", type.toString().toUpperCase());
                            }
                        } else {
                            Object type = JSONPath.eval(button, "$.type");
                            JSONPath.set(button, "$.type", type.toString().toUpperCase());
                        }
                    }
                }
            }

            response = (GetMenuResponse)JSONUtil.toBean(jsonObject.toJSONString(), GetMenuResponse.class);
        } else {
            response = (GetMenuResponse)JSONUtil.toBean(r.toJsonString(), GetMenuResponse.class);
        }

        return response;
    }
}
