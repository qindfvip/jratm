package com.jratm;

import com.jfinal.core.JFinal;

public class ATMMian {

	public static void main(String[] args) {
		JFinal.start("WebRoot", 8080, "/", 5);
	}

}
