package com.base.widget;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.base.common.utils.xx;
import com.base.config.PageConst;
import com.base.engine.EovaExp;
import com.base.model.MetaItem;
import com.base.model.MetaObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * CRUD模板 业务
 * 
 * 
 */
public class WidgetManager {

	/**
	 * 获取排序
	 * 
	 * @param c Controller
	 * @param crud CRUDVO
	 * @return
	 */
	public static String getSort(Controller c, MetaObject eo) {
		// 获取排序字段
		String sort = c.getPara(PageConst.SORT, "");
		if (xx.isEmpty(sort)) {
			return "";
		}
		// if (!xx.isEmpty(eo) ) {
		// if (xx.isEmpty(sort)) {
		// // 初始默认主键排序
		// if (xx.isEmpty(sort) && eo.getBoolean("isDefaultPkDesc")) {
		// return " order by " + eo.getStr("pkName") + " desc";
		// }
		// return " ";
		// }
		// }
		// 获取排序方式
		String order = c.getPara(PageConst.ORDER, "desc");
		return " order by " + sort + ' ' + order;
	}

	public static String getSort(Controller c) {
		return getSort(c, null);
	}

	/**
	 * 获取条件
	 * 
	 * @param c
	 * @param eo
	 * @param eis
	 * @param parmList SQL参数
	 * @return
	 */
	public static String getWhere(Controller c, List<MetaItem> eis, List<String> parmList, String where) {

		boolean isWhere = true;
		for (MetaItem ei : eis) {

			// 给查询表单添加前缀，防止和系统级别字段重名
			String key = ei.getStr("en");
			String value = c.getPara(PageConst.QUERY + key, "").trim();
			if ( (!value.equals("")) && (!value.equals("-1"))) {
				if (isWhere) {
					if (!xx.contains(where.toLowerCase(), "where")) {
						where += " where 1=1 ";
					} else {
						where += " ";
					}
					isWhere = false;
				}
				// 复选框需要特转换值
				if (ei.getStr("type").equals(MetaItem.TYPE_CHECK)) {
					if (xx.isEmpty(value)) {
						value = "0";
					} else {
						value = "1";
					}
				}
				// 文本框查询条件为模糊匹配
				if (ei.getStr("type").equals(MetaItem.TYPE_TEXT)) {
					where += "and " + key + " like ? ";
					parmList.add("%"+ value +"%");
				} else {
					where += "and " + key + " = ? ";
					parmList.add(value);
				}
				// if (ei.getStr("dataType").equals(TemplateConfig.DATATYPE_NUMBER)) {
				// where += "and " + key + " = ? ";
				// parmList.add(value);
				// }
				// if (ei.getStr("dataType").equals(TemplateConfig.DATATYPE_TIME)) {
				// where += "and " + key + " = ? ";
				// parmList.add(value);
				// }
				// 保持条件值
				ei.put("value", value);
			}
		}
		return where;
	}

	/**
	 * 向SQL添加where关键字
	 * 
	 * @param where
	 * @return
	 */
	public static String addWhere(String sql) {
		if (!xx.contains(sql.toLowerCase(), "where")) {
			return " where 1=1 ";
		}
		return "";
	}

	public static void convertValueByExp(List<MetaItem> eis, List<Record> reList) {
		// 根据表达式翻译显示CN(获取当前字段所有的 查询结果值，拼接成 字符串 用于 结合表达式进行 in()查询获得cn 然后替换之)
		for (MetaItem ei : eis) {
			// 获取控件表达式
			String exp = ei.getStr("exp");
			if (xx.isEmpty(exp)) {
				continue;
			}
			// 获取存在表达式的列名
			String en = ei.get("en").toString();
			// System.out.println(en + " EovaExp:" + exp);
			// in 条件值
			Set<String> ids = new HashSet<String>();
			if (!xx.isEmpty(reList)) {
				for (Record re : reList) {
					ids.add(String.valueOf(re.get(en)));
				}
			}

			// 查询本次所有翻译值
			StringBuilder sql = new StringBuilder();
			// 根据表达式获取SQL进行查询
			String select = EovaExp.getSelectNoAlias(exp);
			String from = EovaExp.getFrom(exp);
			String where = EovaExp.getWhere(exp);
			String ds = EovaExp.getDs(exp);
			// 中文列名
			String pk = EovaExp.getPk(exp);
			String cn = EovaExp.getCn(exp);

			sql.append(select);
			sql.append(from);
			sql.append(where);
			sql.append(addWhere(sql.toString()));
			if (!xx.isEmpty(ids)) {
				sql.append(" and ").append(pk);
				sql.append(" in(");
				// 根据当前页数据主键查询外键显示值
				for(String id : ids){
					sql.append(xx.format(id));
					sql.append(",");
				}
				sql.delete(sql.length() -1, sql.length());
				sql.append(")");
			}
			// System.out.println("convertValueByExp：" + sql.toString());

			List<Record> distList = Db.use(ds).find(sql.toString());

			for (Record re : reList) {
				for (Record dis : distList) {
					// 假如Find结果数据中的主键的值，和当前要翻译的列的值相同，说明需要翻译
					String reStr =  String.valueOf(re.get(en));
					String disStr =  String.valueOf(dis.get(pk));
					
					if (reStr !=null && disStr !=null && reStr.equals(disStr)) {
						// 将en的值替换成对应的文字
						// System.out.println(re);
						// re.put("pk", re.get(en).toString());

						re.set(en, dis.get(cn));
						// System.out.println(en);
						// System.out.println(find.get(cn));
						// System.out.println(re);
					}
				}
			}

		}
	}

}