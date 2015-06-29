package com.base.widget.grid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.base.common.Easy;
import com.base.common.render.XlsRender;
import com.base.common.utils.SystemOut;
import com.base.common.utils.xx;
import com.base.config.PageConst;
import com.base.model.MetaItem;
import com.base.model.MetaObject;
import com.base.widget.WidgetManager;
import com.base.widget.WidgetUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * Grid组件
 * 
 * 
 */
public class GridController extends Controller {
	/**
	 * 分页查询
	 */
	public void query() {

		// Get MetaObject Code
		String code = getPara(0);

		// Get MetaObject and MetaItem List
		MetaObject eo = MetaObject.dao.getByCode(code);
		List<MetaItem> eis = MetaItem.dao.queryByObjectCode(code);

		// 获取分页参数
		int pageNumber = getParaToInt(PageConst.PAGENUM, 1);
		int pageSize = getParaToInt(PageConst.PAGESIZE, 95);

		// 获取条件
		List<String> parmList = new ArrayList<String>();
		String where = WidgetManager.getWhere(this, eis, parmList, ' ' + eo.getStr("filterWhere"));

		// 转换SQL参数为Obj[]
		Object[] parm = new Object[parmList.size()];
		parmList.toArray(parm);
		// 获取排序
		String sort = WidgetManager.getSort(this, eo);

		// 分页查询Grid数据
		String view = eo.getView();
		String sql = "from " + view + where + sort;
		Page<Record> page = Db.use(eo.getDs()).paginate(pageNumber, pageSize, "select *", sql, parm);

		// 备份Value列，然后将值列转换成Key列
		WidgetUtil.copyValueColumn(page.getList(), eo.getPk(), eis);
		// 根据表达式将数据中的值翻译成汉字
		WidgetManager.convertValueByExp(eis, page.getList());

		// 将分页数据转换成JSON
		String json = JsonKit.toJson(page.getList());
		json = "{\"total\":" + page.getTotalRow() + ",\"rows\":" + json + "}";
		// SystemOut.println(json);

		renderJson(json);
	}

	/**
	 * 新增
	 */
	public void add() {
		String objectCode = getPara(0);
		MetaObject object = MetaObject.dao.getByCode(objectCode);
		List<MetaItem> items = MetaItem.dao.queryByObjectCode(objectCode);

		String json = getPara("rows");
		SystemOut.println(json);

		List<Record> records = getRecordsByJson(json, items, object.getPk());
		for (Record re : records) {
			Db.use(object.getDs()).save(object.getTable(), object.getPk(), re);
		}

		renderJson(new Easy());
	}

	/**
	 * 删除
	 */
	public void delete() {
		String objectCode = getPara(0);
		MetaObject object = MetaObject.dao.getByCode(objectCode);
		List<MetaItem> items = MetaItem.dao.queryByObjectCode(objectCode);

		String json = getPara("rows");
		SystemOut.println(json);

		List<Record> records = getRecordsByJson(json, items, object.getPk());
		for (Record re : records) {
			Db.use(object.getDs()).delete(object.getTable(), object.getPk(), re);
		}

		renderJson(new Easy());
	}

	/**
	 * 更新 Json:[{"id":1,"loginId":"111"},{"id":2,"loginId":"222"}]
	 * 
	 * @throws IOException
	 */
	public void update() throws IOException {

		String objectCode = getPara(0);
		MetaObject object = MetaObject.dao.getByCode(objectCode);
		List<MetaItem> items = MetaItem.dao.queryByObjectCode(objectCode);

		String json = getPara("rows");
		SystemOut.println(json);

		List<Record> records = getRecordsByJson(json, items, object.getPk());
		for (Record re : records) {
			Db.use(object.getDs()).update(object.getTable(), object.getPk(), re);
		}

		renderJson(new Easy());
	}
	
	/**
	 * 导出
	 */
	public void export() {
		String objectCode = getPara(0);
		MetaObject object = MetaObject.dao.getByCode(objectCode);
		List<MetaItem> items = MetaItem.dao.queryByObjectCode(objectCode);
		List<Record> data = Db.use(object.getDs()).find("select * from " + object.getTable());
		render(new XlsRender(data, items, object));
	}

	/**
	 * json转List
	 * 
	 * @param json
	 * @param pkName TODO
	 * @return
	 */
	private static List<Record> getRecordsByJson(String json, List<MetaItem> items, String pkName) {
		List<Record> records = new ArrayList<Record>();

		List<JSONObject> list = JSON.parseArray(json, JSONObject.class);
		for (JSONObject o : list) {
			Map<String, Object> map = JSON.parseObject(o + "", new TypeReference<Map<String, Object>>() {
			});
			Record re = new Record();
			re.setColumns(map);
			// 将Text翻译成Value,然后删除val字段
			for (MetaItem x : items) {
				String en = x.getStr("en");// 字段名
				String exp = x.getStr("exp");// 表达式
				String type = x.getStr("type");// 控件类型
				Object value = re.get(en);// 值

				if (!xx.isEmpty(exp)) {
					String valField = en + "_val";
					// 获取值列中的值
					value = re.get(valField).toString();
					// 获得值之后删除值列防止持久化报错
					re.remove(valField);
				}
				// 复选框需要特转换值
				if (type.equals(MetaItem.TYPE_CHECK)) {
					value = Boolean.parseBoolean(value.toString());
				}
				re.set(en, value);
			}
			// 删除主键备份值列
			re.remove("pk_val");
			records.add(re);
		}

		return records;
	}

	public static void main(String[] args) {

		String sl = "[{'id':1,'loginId':'111'},{'id':2,'loginId':'222'}]";
		List<JSONObject> list = JSON.parseArray(sl, JSONObject.class);
		for (JSONObject o : list) {
			Map<String, Object> map = JSON.parseObject(o + "", new TypeReference<Map<String, Object>>() {
			});
			Record re = new Record();
			re.setColumns(map);
			SystemOut.println(re.toJson());
		}
	}

}