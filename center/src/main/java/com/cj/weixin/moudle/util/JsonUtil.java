package com.cj.weixin.moudle.util;

import com.alibaba.fastjson.JSON;
import com.cj.weixin.gzh.GzhEntity;
import com.cj.weixin.menu.MenuEntity;

import java.util.List;

public class JsonUtil {

	/**
	 * 将字符串gzhs转换成List<GzhEntity>
	 * @param gzhs 公众号字符串jsonArray
	 * @return List<GzhEntity>
	 */
	public static List<GzhEntity> getGzhs(String gzhs){
		return JSON.parseArray(gzhs, GzhEntity.class);
	}
	
	/**
	 * 将字符串menus转换成List<MenuEntity>
	 * @param menus 菜单字符串jsonArray
	 * @return List<MenuEntity>
	 */
	public static List<MenuEntity> getMenus(String menus){
		return JSON.parseArray(menus, MenuEntity.class);
	}
}
