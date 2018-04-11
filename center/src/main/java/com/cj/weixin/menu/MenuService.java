package com.cj.weixin.menu;

import com.cj.weixin.gzh.GzhEntity;
import com.cj.weixin.gzh.GzhService;
import com.cj.weixin.moudle.util.JsonUtil;
import com.cj.weixin.moudle.util.WeixinUtil;
import com.github.sd4324530.fastweixin.api.MenuAPI;
import com.github.sd4324530.fastweixin.api.config.ApiConfig;
import com.github.sd4324530.fastweixin.api.entity.Menu;
import com.github.sd4324530.fastweixin.api.entity.MenuButton;
import com.github.sd4324530.fastweixin.api.enums.MenuType;
import com.github.sd4324530.fastweixin.api.enums.ResultType;
import com.github.sd4324530.fastweixin.api.response.GetMenuResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

@Service
public class MenuService {
    private Logger log = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    GzhService gzhService;

    @Value("${chujiu.gzhs}")
    private String gzhJsonArr;

    @Value("${chujiu.menus}")
    private String menusJsonArr;


    /**
     * 获取菜单中tp和当前tp一样的菜单
     *
     * @param listAll 所有菜单
     * @param tp      公众号tp
     * @return List<MenuEntity>
     */
    public List<MenuEntity> getTpMenus(List<MenuEntity> listAll, String tp) {
        //当前tp下的公众号菜单
        List<MenuEntity> listTp = new ArrayList<MenuEntity>();
        for (MenuEntity menuEntity : listAll) {
            //获取菜单中tp和当前tp一样的菜单
            if (tp.equals(menuEntity.getMenuTp())) {
                listTp.add(menuEntity);
            }
        }
        return listTp;
    }

    /**
     * 更新微信公众号菜单
     */
    @PostConstruct
    public void syncWeixinMenu() {
        if (WeixinUtil.gzhTokenMap.size() == 0) {
            List<GzhEntity> gzhList = JsonUtil.getGzhs(gzhJsonArr);
            for (GzhEntity gzhEntity : gzhList) {
                WeixinUtil.apiMap.put(gzhEntity.getTp(), new ApiConfig(gzhEntity.getAppid(), gzhEntity.getAppsecret(), true));
                WeixinUtil.gzhTokenMap.put(gzhEntity.getTp(), gzhEntity.getToken());
            }
        }


        //获取所有公众号的菜单目录
        List<MenuEntity> listAll = JsonUtil.getMenus(menusJsonArr);
        for (Entry<String, String> entry : WeixinUtil.gzhTokenMap.entrySet()) {
            //获取菜单中tp和当前tp一样的菜单
            List<MenuEntity> listTp = this.getTpMenus(listAll, entry.getKey());
            //获取公众号配置参数
            ApiConfig apiConfig = WeixinUtil.apiMap.get(entry.getKey());
            MenuAPI menuAPI = WeixinUtil.getMenuAPIMap(entry.getKey());
            Menu request = new Menu();
            request.setButton(new ArrayList<MenuButton>());

            //获取微信服务器上的菜单目录
            MenuAPI menuAPIService = WeixinUtil.getMenuAPIMapImpl(entry.getKey());
            GetMenuResponse menuResponse = menuAPIService.getMenu();

            for (MenuEntity me : listTp) {
                if (null == me.getParentOid()) {
                    // 准备一级主菜单
                    MenuButton main = new MenuButton();
                    main.setType(MenuType.valueOf(me.getMenuType()));
                    main.setKey(me.getMenuKey());
                    main.setName(me.getMenuName());
                    main.setUrl(this.buildReqUrl(apiConfig.getAppid(), entry.getKey(), me.getMenuUrl()));

                    // 二级菜单
                    List<MenuButton> subs = new ArrayList<MenuButton>();
                    for (MenuEntity me1 : listTp) {
                        if (!"".equals(me.getParentOid()) && me.getOid().equals(me1.getParentOid())) {
                            MenuButton sub = new MenuButton();
                            sub.setType(MenuType.valueOf(me1.getMenuType()));
                            sub.setKey(me1.getMenuKey());
                            sub.setName(me1.getMenuName());
                            sub.setUrl(this.buildReqUrl(apiConfig.getAppid(), entry.getKey(), me1.getMenuUrl()));
                            subs.add(sub);
                        }
                    }
                    main.setSubButton(subs);
                    request.getButton().add(main);
                }
                break;//配置文件里面的菜单只更新第一个
            }

            if (menuResponse != null && menuResponse.getMenu() != null && menuResponse.getMenu().getButton() != null && menuResponse.getMenu().getButton().size() > 0) {
                for (int i = 0; i < menuResponse.getMenu().getButton().size(); i++) {
                    if (i > 0) {//添加原来菜单的
                        request.getButton().add(menuResponse.getMenu().getButton().get(i));
                    }
                }

            }

            log.debug(request.toJsonString());
            // 先删除之前的菜单
            menuAPI.deleteMenu();
            // 创建菜单
            ResultType resultType = menuAPI.createMenu(request);
            log.debug(resultType.toString());
        }
    }

    /**
     * 构建页面请求URL
     *
     * @param appid
     * @param tp
     * @param url
     * @return
     */
    private String buildReqUrl(String appid, String tp, String url) {
        if (url != null && !"".equals(url)) {
            String req_url = "";
            try {
                req_url = URLEncoder.encode(url, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri=" + req_url + "&response_type=code&scope=snsapi_userinfo&state=" + tp
                    + "#wechat_redirect";
        } else {
            return null;
        }

    }
}
