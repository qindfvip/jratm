package com.base.core.auth;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.base.common.Easy;
import com.base.common.utils.xx;
import com.base.config.AppConst;
import com.base.model.Button;
import com.base.model.Menu;
import com.base.model.RoleBtn;
import com.base.service.sm;
import com.base.widget.WidgetUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 元数据操作 MetaObject+MetaItem
 * 
 */
public class AuthController extends Controller {

	private final String PATH = AppConst.PATH;
	/**
	 * 菜单基本功能管理
	 */
	public void toRoleChoose() {
		setAttr("rid", getPara(0));

		render("/"+PATH+"/auth/roleChoose.html");
	}

	/**
	 * 获取功能JSON
	 */
	public void getFunJson() {
		
		// 获取所有菜单信息
		LinkedHashMap<Integer, Record> menus = (LinkedHashMap<Integer, Record>) sm.auth.queryByParentId(0);
		// 获取所有按钮信息
		List<Button> btns = Button.dao.findBtns();

		// 构建菜单对应功能点 eg. [玩家管理] 口查询 口新增 口修改 口删除
		for (Map.Entry<Integer, Record> map : menus.entrySet()) {
			buildBtn(map.getValue(), btns);
		}

		// Map 转 Tree Json
		String json = WidgetUtil.maptoTreeJsons(menus);

		renderJson(json);
	}

	private void buildBtn(Record record, List<Button> btns) {
		String code = record.getStr("code");
		if (code.equals(Menu.TYPE_DIR) || code.equals(Menu.TYPE_DIY)) {
			// 忽略自定义URL和目录
			return;
		}

		String btnId = "", btnName = "";
		for (Button btn : btns) {
			if (btn.getStr("menuCode").equals(code)) {
				btnId += btn.getInt("id") + ",";
				btnName += btn.getStr("name") + ",";
			}
		}
		if (xx.isEmpty(btnId)) {
			return;
		}
		if (btnId.endsWith(",")) {
			btnId = btnId.substring(0, btnId.length() - 1);
			btnName = btnName.substring(0, btnName.length() - 1);
		}
		record.set("btnId", btnId);
		record.set("btnName", btnName);
	}

	/**
	 * 获取角色已分配功能JSON
	 */
	public void getRoleFunJson() {
		int rid = getParaToInt(0);
		if (xx.isEmpty(rid)) {
			renderJson(new Easy("参数缺失!"));
			return;
		}
		List<RoleBtn> list = RoleBtn.dao.queryByRid(rid);
		String json = JsonKit.toJson(list);
		System.out.println(json);
		renderJson(json);
	}

	/**
	 * 授权
	 */
	public void roleChoose() {
		int rid = getParaToInt(0);
		if (xx.isEmpty(rid)) {
			renderJson(new Easy("参数缺失!"));
			return;
		}
		// 获取选中功能点
		String checks = getPara("checks");
		// 删除历史授权
		Db.use(xx.DS_BASE).update("delete from eova_role_btn where rid = ?", rid);
		if (xx.isEmpty(checks)) {
			renderJson(new Easy());
			return;
		}
		String[] ids = checks.split(",");
		for (String id : ids) {
			RoleBtn rf = new RoleBtn();
			rf.set("rid", rid);
			// if (id.startsWith("btn_")) {
			// rf.set("isBtn", 1);
			// id = id.replace("btn_", "");
			// }
			rf.set("bid", id);
			rf.save();
		}

		// 保存授权
		// Role role = Role.dao.findById(rid);
		// role.set("fun", checks);
		// role.update();

		renderJson(new Easy());
	}

	
	
	
}