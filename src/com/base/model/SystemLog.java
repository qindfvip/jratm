package com.base.model;

import com.base.common.base.BaseModel;
import com.base.common.utils.web.RequestUtil;
import com.jfinal.core.Controller;

/**
 * 系统操作日志
 *
 */
public class SystemLog extends BaseModel<SystemLog> {

	private static final long serialVersionUID = -1592533967096109392L;

	public static final SystemLog dao = new SystemLog();

	/** 新增 **/
	public static final int ADD = 1;
	/** 修改 **/
	public static final int UPDATE = 2;
	/** 删除 **/
	public static final int DELETE = 3;
	/** 导入 **/
	public static final int IMPORT = 4;
	
	/**
	 * 操作日志
	 * @param con
	 * @param info 日志详情
	 */
	public void info(Controller con, int type, String info){
		SystemLog el = new SystemLog();
		// TYPE
		el.set("type", type);
		// UID
		User user = con.getSessionAttr("user");
		el.set("uid", user.getInt("id"));
		// IP
		String ip = RequestUtil.getIp(con.getRequest());
		el.set("ip", ip);
		el.set("info", info);
		el.save();
	}
}