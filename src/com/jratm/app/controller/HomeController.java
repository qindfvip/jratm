package com.jratm.app.controller;


import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;



public class HomeController extends Controller {


	//@ActionKey("/login")
	public void index() {
		renderText("Hello JFinal home World.");
	}

}
