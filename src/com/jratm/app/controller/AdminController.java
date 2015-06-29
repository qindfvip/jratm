package com.jratm.app.controller;

import com.jfinal.core.Controller;


//@ControllerBind(controllerKey = "/admin")
public class AdminController extends Controller {


	//@ActionKey("/login")
	
	public void index() {
		renderText("Hello admin JFinal World.");
	}

}
