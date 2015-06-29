package com.base.interceptor;

import java.util.List;

import com.base.model.Menu;
import com.base.model.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

/**
 * 权限验证
 *
 */
public class AuthInterceptor implements Interceptor {

	@Override
	@SuppressWarnings("unused")
	public void intercept(ActionInvocation ai) {
		String actionKey = ai.getActionKey();
		String uri = ai.getController().getRequest().getRequestURI();

		// 查询当前角色已授权菜单关联有效对象的集合
		if (actionKey.startsWith("/crud/toList")) {
			User user = ai.getController().getSessionAttr("user");
			int rid = user.getInt("rid");

			// 查询当前角色已授权菜单URL
			List<Menu> menus = Menu.dao.findMunus(rid);
			
	
		}

		ai.invoke();
		

	}

}