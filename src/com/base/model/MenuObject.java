package com.base.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.base.common.base.BaseModel;
import com.base.common.utils.xx;

/**
 * 菜单关联对象
 * 
 */
public class MenuObject extends BaseModel<MenuObject> {

	private static final long serialVersionUID = 9176734392973431592L;

	public static final MenuObject dao = new MenuObject();

	/**
	 * 获取菜单关联对象
	 * @param menuCode
	 * @return
	 */
	public List<MenuObject> queryByMenuCode(String menuCode) {
		return MenuObject.dao.queryByCache("select objectCode from "+getTableName()+" where menuCode = ?", menuCode);
	}
	
	/**
	 * 删除菜单关联数据对象
	 * @param menuCode
	 */
	public void deleteByMenuCode(String menuCode) {
		Db.use(xx.DS_BASE).update("delete from "+getTableName()+" where menuCode = ?", menuCode);
	}
	
}