package com.cj.weixin.menu;

import java.io.Serializable;

public class MenuEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String oid;
	//所属tp
	public String menuTp;
	//菜单key
	public String menuKey;
	//名称
	public String menuName;
	//类型,如:CLICK,VIEW
	public String menuType;
	//跳转路径
	public String menuUrl;
	//一级菜单oid
	public String parentOid;
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getMenuTp() {
		return menuTp;
	}
	public void setMenuTp(String menuTp) {
		this.menuTp = menuTp;
	}
	public String getMenuKey() {
		return menuKey;
	}
	public void setMenuKey(String menuKey) {
		this.menuKey = menuKey;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public String getParentOid() {
		return parentOid;
	}
	public void setParentOid(String parentOid) {
		this.parentOid = parentOid;
	}
}
