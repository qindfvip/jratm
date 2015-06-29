package com.base.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.base.common.base.BaseCache;
import com.base.common.base.BaseService;
import com.base.common.utils.xx;
import com.base.config.AppConst;
import com.base.model.Menu;
import com.base.model.RoleBtn;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 权限数据访问
 * 
 * 
 */
public class AuthService extends BaseService {

	/**
	 * 根据角色ID获取已授权查询的菜单Code
	 * 
	 * @param rid 角色ID
	 * @return
	 */
	public List<String> queryMenuCodeByRid(int rid) {
		return RoleBtn.dao.queryMenuCodeByRid(rid);
	}

	/**
	 * 查询某棵树下是否存在已授权的功能
	 * 
	 * @param parentId 父节点ID
	 * @param rid　角色ID
	 * @return
	 */
	public boolean isExistsAuthByPidRid(int parentId, int rid) {
		// 根据角色ID获取已授权查询的菜单Code
		List<String> menuCodes = queryMenuCodeByRid(rid);
		LinkedHashMap<Integer, Record> result = (LinkedHashMap<Integer, Record>) queryByParentId(parentId);
		for (String menuCode : menuCodes) {
			for (Map.Entry<Integer, Record> map : result.entrySet()) {
				if (map.getValue().getStr("code").equals(menuCode)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 根据父ID获取该节点及下属所有数据
	 * 
	 * @param parentId 父节点ID
	 * @return
	 */
	public List<Menu> queryMenuByParentId(int parentId) {
		LinkedHashMap<Integer, Record> result = (LinkedHashMap<Integer, Record>) queryByParentId(parentId);
		List<Menu> menus = new ArrayList<Menu>();
		for (Map.Entry<Integer, Record> map : result.entrySet()) {
			Menu menu = new Menu();
			menu.setAttrs(map.getValue().getColumns());
			menus.add(menu);
		}
		return menus;
	}

	/**
	 * 获取某节点所有父子数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, Record> queryByParentId(int parentId) {
		LinkedHashMap<Integer, Record> temp = null;

		// 取Cache，没有再查库
		Object obj = BaseCache.getSer(AppConst.ALL_MENU);
		if (xx.isEmpty(obj)) {
			// 获取所有菜单信息
			List<Record> records = Menu.dao.findAll();

			// List转有序Map
			temp = new LinkedHashMap<Integer, Record>();
			for (Record x : records) {
				// 获取EasyUI所需ICON字段名
				String icon = x.get("icon");
				if (xx.isEmpty(icon)) {
					icon = "icon-application";
				}
				x.set("icon", icon);

				temp.put(x.getInt("id"), x);
			}

			// Cache 转换后的菜单信息
			// BaseCache.putSer(EovaConst.ALL_MENU, temp);
		} else {
			temp = new LinkedHashMap<Integer, Record>();
			temp.putAll((LinkedHashMap<Integer, Record>) obj);
		}

		// 获取某节点下所有数据
		LinkedHashMap<Integer, Record> result = new LinkedHashMap<Integer, Record>();
		// 递归获取子节点
		getChildren(temp, parentId, result);

		return result;
	}

	/**
	 * 递归查找子节点
	 * 
	 * @param all 所有菜单
	 * @param parentId 父节点ID
	 * @param result 节点下所有数据(包括父)
	 */
	private void getChildren(Map<Integer, Record> all, int parentId, Map<Integer, Record> result) {
		for (Map.Entry<Integer, Record> map : all.entrySet()) {
			// 获取父节点
			if (map.getKey() == parentId) {
				result.put(map.getKey(), map.getValue());
			}
			// 获取子节点
			if (map.getValue().getInt("parentId") == parentId) {
				result.put(map.getKey(), map.getValue());
				// 子ID递归找孙节点
				getChildren(all, map.getKey(), result);
			}
		}
	}
}