package com.base.interceptor;

import org.apache.commons.lang3.StringUtils;

import com.base.config.AppConst;
import com.base.model.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

/**
 * 常量加载拦截器
 *
 */
public class LoginInterceptor implements Interceptor{

	public void intercept(ActionInvocation ai) {
		
		String path_tmp = ai.getActionKey();

		if (path_tmp.equals("/toLogin")) {
			ai.invoke();
			return;
		}
		if (path_tmp.equals("/vcodeImg")) {
			ai.invoke();
			return;
		}
		if (path_tmp.equals("/doLogin")) {
			ai.invoke();
			return;
		}
		
		if (path_tmp.startsWith("/")) {
			path_tmp = path_tmp.substring(1, path_tmp.length());
		}
		if (path_tmp.endsWith("/")) {
			path_tmp = path_tmp.substring(0, path_tmp.length() - 1);
		}
		
		if(isAuth(path_tmp)) {
			// 获取登录用户的角色
			User user = ai.getController().getSessionAttr("user");
			if (user == null) {
				ai.getController().redirect(AppConst.ADMIN_PATH + "/toLogin");
				return;
			}
		}
		
		ai.invoke();
	}
	
	/**
	 * 认证方法
	 * 
	 * @param path_tmp
	 * @return
	 */
	protected boolean isAuth(String path_tmp) {
		return StringUtils.isNotBlank(path_tmp) //
				&& path_tmp.indexOf("login") < 0 // 登录
				&& !path_tmp.endsWith("logout") // 登出
				&& !path_tmp.startsWith("admin") // 登录
		;
	}

}