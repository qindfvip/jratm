package com.base.core.menu;

import com.base.common.Easy;
import com.base.common.base.BaseCache;
import com.base.common.utils.xx;
import com.base.config.AppConst;
import com.base.model.Button;
import com.base.model.Menu;
import com.base.model.MenuObject;
import com.base.model.RoleBtn;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 菜单管理
 * 
 */
public class MenuController extends Controller {
	
	private final String PATH = AppConst.PATH;

	public void toAdd() {
		render("/"+PATH+"/menu/form.html");
	}
	
	public void toUpdate() {
		int pkValue = getParaToInt(1);
		Menu menu = Menu.dao.findById(pkValue);
		
		setAttr("menu", menu);

		render("/"+PATH+"/menu/form.html");
	}

	/**
	 * 菜单基本功能管理
	 */
	public void toMenuFun() {
		String menuCode = getPara(0);
		setAttr("menuCode", menuCode);

		setAttr("isAdd", Button.dao.isExistButton(menuCode, AppConst.FUN_ADD_BS));
		setAttr("isUpdate", Button.dao.isExistButton(menuCode, AppConst.FUN_UPDATE_BS));
		setAttr("isDelete", Button.dao.isExistButton(menuCode, AppConst.FUN_DELETE_BS));
		setAttr("isImport", Button.dao.isExistButton(menuCode, AppConst.FUN_IMPORT_BS));

		render("/"+PATH+"/menu/menuFun.html");
	}

	/**
	 * 新增菜单
	 */
	@Before(Tx.class)
	public void add() {
		
		String menuCode = getPara("code");
		String type = getPara("type");
		
		Menu menu = new Menu();
		menu.set("parentId", getPara("parentId"));
		menu.set("icon", getPara("icon",""));
		menu.set("name", getPara("name"));
		menu.set("code", menuCode);
		menu.set("indexNum", getPara("indexNum"));
		menu.set("type", type);
		menu.set("bizIntercept", getPara("bizIntercept", ""));
		menu.set("url", getPara("url", ""));
		menu.save();
		
		// 新增菜单使缓存失效 
		BaseCache.delSer(AppConst.ALL_MENU);
		
		// 如果是父级目录菜单没有按钮也无需关联对象
		if (type.equals(Menu.TYPE_DIR)) {
			renderJson(new Easy());
			return;
		}
		
		// 初始化查询按钮
		{
			Button btn = new Button();
			btn.set("menuCode", menuCode);
			btn.set("name", AppConst.FUN_QUERY);
			btn.save();
			// 自动授权给超级管理员
			RoleBtn rf = new RoleBtn();
			rf.set("rid", AppConst.DEFAULT_RID);
			rf.set("bid", btn.getInt("id"));
			rf.save();
		}
		// 添加
		{
			Button btn = new Button();
			btn.set("menuCode", menuCode);
			btn.set("name", AppConst.FUN_ADD);
			btn.set("ui", AppConst.FUN_ADD_UI);
			btn.set("bs", AppConst.FUN_ADD_BS);
			btn.save();
			// 自动授权给超级管理员
			RoleBtn rf = new RoleBtn();
			rf.set("rid", AppConst.DEFAULT_RID);
			rf.set("bid", btn.getInt("id"));
			rf.save();
		}
		// 修改
		{
			Button btn = new Button();
			btn.set("menuCode", menuCode);
			btn.set("name", AppConst.FUN_UPDATE);
			btn.set("ui", AppConst.FUN_UPDATE_UI);
			btn.set("bs", AppConst.FUN_UPDATE_BS);
			btn.save();
			// 自动授权给超级管理员
			RoleBtn rf = new RoleBtn();
			rf.set("rid", AppConst.DEFAULT_RID);
			rf.set("bid", btn.getInt("id"));
			rf.save();
		}
		// 删除
		{
			Button btn = new Button();
			btn.set("menuCode", menuCode);
			btn.set("name", AppConst.FUN_DELETE);
			btn.set("ui", AppConst.FUN_DELETE_UI);
			btn.set("bs", AppConst.FUN_DELETE_BS);
			btn.save();
			// 自动授权给超级管理员
			RoleBtn rf = new RoleBtn();
			rf.set("rid", AppConst.DEFAULT_RID);
			rf.set("bid", btn.getInt("id"));
			rf.save();
		}
		
		// 单表-菜单关联对象
		if (type.equals(Menu.TYPE_SINGLEGRID)) {
			// 单表只有一个对象
			MenuObject mo = new MenuObject();
			mo.set("menuCode", menuCode);
			mo.set("objectCode", getPara("objectCode"));
			mo.save();
		}
		
		// TODO 其它业务模版
		
		renderJson(new Easy());
	}

	/**
	 * 菜单功能管理
	 */
	public void menuFun() {
		String menuCode = getPara(0);

		String isAdd = getPara("isAdd");
		String isUpdate = getPara("isUpdate");
		String isDelete = getPara("isDelete");
		String isImport = getPara("isImport");

		// 删除当前菜单的基本按钮然后重新添加
		Button.dao.deleteFunByMenuCode(menuCode);

		// 添加
		if (!xx.isEmpty(isAdd)) {
			Button btn = new Button();
			btn.set("menuCode", menuCode);
			btn.set("name", AppConst.FUN_ADD);
			btn.set("ui", AppConst.FUN_ADD_UI);
			btn.set("bs", AppConst.FUN_ADD_BS);
			btn.save();
		}
		// 修改
		if (!xx.isEmpty(isUpdate)) {
			Button btn = new Button();
			btn.set("menuCode", menuCode);
			btn.set("name", AppConst.FUN_UPDATE);
			btn.set("ui", AppConst.FUN_UPDATE_UI);
			btn.set("bs", AppConst.FUN_UPDATE_BS);
			btn.save();
		}
		// 删除
		if (!xx.isEmpty(isDelete)) {
			Button btn = new Button();
			btn.set("menuCode", menuCode);
			btn.set("name", AppConst.FUN_DELETE);
			btn.set("ui", AppConst.FUN_DELETE_UI);
			btn.set("bs", AppConst.FUN_DELETE_BS);
			btn.save();
		}
		// 导入
		if (!xx.isEmpty(isImport)) {
			Button btn = new Button();
			btn.set("menuCode", menuCode);
			btn.set("name", AppConst.FUN_IMPORT);
			btn.set("ui", AppConst.FUN_IMPORT_UI);
			btn.set("bs", AppConst.FUN_IMPORT_BS);
			btn.save();
		}

		renderJson(new Easy());
	}

}