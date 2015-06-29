package com.base.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.base.common.base.BaseModel;
import com.base.common.utils.xx;

/**
 * 角色已授权功能点
 * 
 */
public class RoleBtn extends BaseModel<RoleBtn> {

	private static final long serialVersionUID = -1794335434198017392L;

	public static final RoleBtn dao = new RoleBtn();

	
	/**
	 * 根据角色ID获取已授权查询的菜单Code
	 * 
	 * @param rid 角色ID
	 * @return
	 */
	public List<String> queryMenuCodeByRid(int rid) {
		String sql = "select DISTINCT(b.menuCode) from "+getTableName()+" rf LEFT JOIN "+getTableName(Button.class)+" b on rf.bid = b.id where b.name = '查询' and rf.rid = ?";
		return Db.use(xx.DS_BASE).query(sql, rid);
	}
	
	/**
	 * 获取角色授权信息
	 * 
	 * @param rid 角色ID
	 * @return
	 */
	public List<RoleBtn> queryByRid(int rid) {
		return super.find("select * from "+getTableName()+" where rid = ?", rid);
	}

	/**
	 * 删除菜单功能关联的权限
	 * @param menuCode
	 */
	public void deleteByMenuCode(String menuCode){
		String sql = "delete from "+getTableName()+" where bid in (select id from "+getTableName(Button.class)+" where menuCode = ?)";
		Db.use(xx.DS_BASE).update(sql, menuCode);
	}
	
}