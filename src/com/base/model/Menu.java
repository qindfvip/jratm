package com.base.model;

import java.util.List;

import com.base.common.base.BaseModel;
import com.base.common.utils.xx;
import com.base.config.AppConst;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class Menu extends BaseModel<Menu> {

	private static final long serialVersionUID = 7072369370299999169L;

	public static final Menu dao = new Menu();

	/** 菜单类型-目录 **/
	public static final String TYPE_DIR = "dir";
	/** 菜单类型-自定义 **/
	public static final String TYPE_DIY = "diy";
	/** 菜单类型-单表 **/
	public static final String TYPE_SINGLEGRID = "singleGrid";

	private List<Menu> childList;

	public List<Menu> getChildList() {
		return childList;
	}

	public void setChildList(List<Menu> childList) {
		this.childList = childList;
	}

	/**
	 * 获取访问URL
	 */
	public String getUrl() {
		String url = this.getStr("url");
		if (xx.isEmpty(url)) {
			return AppConst.ADMIN_PATH + '/' + this.getStr("type") + "/list/" + this.getStr("code");
		}
		return url;
	}

	// 查询当前角色已授权菜单URL
	public List<Menu> findMunus(int rid) {

		StringBuilder sb = new StringBuilder();
		sb.append("select m.urlCmd from " + getTableName(Button.class) + " b");
		sb.append(" LEFT JOIN " + getTableName(RoleBtn.class) + " rf on rf.bid = b.id");
		sb.append(" LEFT JOIN " + getTableName(Menu.class) + " m on b.menuCode = m.code");
		sb.append(" where rid = ? and b.name = '查询'");
		return Menu.dao.find(sb.toString(), rid);

	}

	// 获取所有菜单信息
	// select * from order by parentId,indexNum desc
	public List<Record> findAll() {

		String sql = "select * from " + getTableName() + " order by parentId,indexNum";
		return Db.use(xx.DS_BASE).find(sql);

	}

	public Menu findByCode(String code) {
		String sql = "select * from " + getTableName() + " where code = ?";
		return Menu.dao.findFirst(sql, code);
	}

	/**
	 * 获取根节点
	 * 
	 * @return
	 */
	public List<Menu> queryRoot() {
		return super.queryByCache("select * from " + getTableName() + " where parentId = 0 order by indexNum");
	}

	/**
	 * 获取所有节点
	 * 
	 * @return
	 */
	@Override
	public List<Menu> queryAll() {
		return super.queryByCache("select * from " + getTableName() + " order by indexNum");
	}

}