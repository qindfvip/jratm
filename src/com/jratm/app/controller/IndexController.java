package com.jratm.app.controller;

import com.base.config.RouteBind;
import com.jfinal.core.Controller;


/**
 * 
 * 首页入口
 *
 */
@RouteBind(path="/", viewPath = "front/home")
public class IndexController extends Controller {
	
	
	public void index() {
		renderJsp("home.jsp");
	}
}
