package com.base.config;

import com.base.core.IndexController;
import com.base.core.auth.AuthController;
import com.base.core.menu.MenuController;
import com.base.core.object.MetaDataController;
import com.base.template.crud.CrudConfig;
import com.base.template.crud.CrudController;
import com.base.widget.WidgetController;
import com.base.widget.grid.GridController;
import com.jfinal.config.Routes;


/**
 * 
 * 后台路由 
 *
 */
public class AdminRoutes extends Routes{
	
	private static final AdminRoutes admin = new AdminRoutes();

	@Override
	public void config() {

		add(AppConst.ADMIN_PATH, IndexController.class);
		add(AppConst.ADMIN_PATH + "/" + CrudConfig.contro, CrudController.class);
		add(AppConst.ADMIN_PATH + "/widget", WidgetController.class);
		add(AppConst.ADMIN_PATH + "/grid", GridController.class);
		add(AppConst.ADMIN_PATH + "/metadata", MetaDataController.class);
		add(AppConst.ADMIN_PATH + "/menu", MenuController.class);
		add(AppConst.ADMIN_PATH + "/auth", AuthController.class);
		
	}
	
	/**
	 * 获取后台路由实例
	 * 
	 * @return
	 */
	public static AdminRoutes me() {
		return admin;
	}


}
