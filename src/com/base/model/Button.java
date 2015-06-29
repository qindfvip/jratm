package com.base.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.base.common.base.BaseModel;
import com.base.common.utils.xx;
import com.base.config.AppConst;

/**
 * 功能按钮
 */
public class Button extends BaseModel<Button> {

	private static final long serialVersionUID = 3481288366186459644L;

	public static final Button dao = new Button();

	public List<Button> findBtns(){

		return Button.dao.find("select * from "+getTableName()+" order by menuCode,indexNum");
		
	}
	/**
	 * 根据权限获取功能按钮
	 * @param menuCode
	 * @param rid
	 * @return
	 */
	public List<Button> queryByMenuCode(String menuCode, int rid) {
		return Button.dao.find("select * from "+getTableName()+" where menuCode = ? and ui != '' and id in (select bid from "+getTableName(RoleBtn.class)+" where rid = ?) order by indexNum", menuCode, rid);
	}
	
	/**
	 * 是否存在功能按钮
	 * @param menuCode 菜单编码
	 * @param bs 服务端
	 * @return 是否存在该按钮
	 */
	public boolean isExistButton(String menuCode, String bs){
		String sql = "select count(*) from "+getTableName()+" where menuCode = ? and bs = ?";
		long count = Db.use(xx.DS_BASE).queryLong(sql, menuCode, bs);
		if (count != 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 删除功能按钮(除查询按钮外)
	 * @param menuCode
	 */
	public void deleteFunByMenuCode(String menuCode){
		String sql = "delete from "+getTableName()+" where name != ? and menuCode = ?";
		Db.use(xx.DS_BASE).update(sql, AppConst.FUN_QUERY, menuCode);
	}
	
	/**
	 * 删除菜单下所有按钮
	 * @param menuCode
	 */
	public void deleteByMenuCode(String menuCode){
		String sql = "delete from "+getTableName()+" where menuCode = ?";
		Db.use(xx.DS_BASE).update(sql, menuCode);
	}

}