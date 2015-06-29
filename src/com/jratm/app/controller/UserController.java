package com.jratm.app.controller;

import com.base.config.AppConst;
import com.base.config.RouteBind;
import com.jfinal.core.Controller;


@RouteBind(path="/user")
public class UserController extends Controller {

	public void index() {
		render("test/test.html");
		//renderText("Hello jratm World index .");
	}
	
	
	//@ActionKey("/jratm/user/list")
	public void list() {
		renderText("Hello  user list World test .");
	}
	
	/**
	 * 预留前台登录页面,根据需要改动
	 * 
	 */
	public void login() {
		render("/" + AppConst.PATH + "/login.html");
	}

}
