package com.base.service;


/**
 * 数据访问集中管理
 *
 */
public class ServiceManager {
	/**初始数据访问**/
	//public static IndexService index;
	/**用户数据访问**/
	public static UserService user;
	/**EOVA数据访问**/
	public static EovaService eova;
	/**权限数据访问**/
	public static AuthService auth;
	
	public static void init(){
		//index = new IndexService();
		user = new UserService();
		eova = new EovaService();
		auth = new AuthService();
	}
}