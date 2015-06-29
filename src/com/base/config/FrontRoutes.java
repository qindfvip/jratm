package com.base.config;

import com.jfinal.config.Routes;

/**
 * 
 * 前台路由
 *
 */
public class FrontRoutes extends Routes {
	
	private static final FrontRoutes front = new FrontRoutes();

	@Override
	public void config() {

	}
	
	/**
	 * 获取前台路由实例
	 * 
	 * @return
	 */
	public static FrontRoutes me() {
		return front;
	}

}
