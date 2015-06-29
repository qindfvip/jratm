package com.base.template.common.util;

import java.util.List;

import com.base.model.MetaItem;
import com.base.model.MetaObject;
import com.jfinal.plugin.activerecord.Record;

/**
 * UI生成工具类
 * 
 */
public class UIUtil {

	/**
	 * 获取Table内容部分
	 * 
	 * @param obj 对象
	 * @param itemList 字段集合
	 * @param dataList 数据集合
	 * @return
	 */
	public static String getTableBody(MetaObject obj, List<MetaItem> itemList, List<Record> dataList) {
		StringBuilder sb = new StringBuilder();

		// 遍历数据集
		for (Record record : dataList) {
			// 主键
			String pkName = obj.get("pkName");
			// 获取当前数据对象的主键值
			String pkValue = record.get(pkName).toString();

			sb.append("<tr target=\"" + pkName + "\" rel=\"" + pkValue + "\">");
			// 是否允许批量删除
			boolean isBatchDelete = obj.get("isBatchDelete");
			if (isBatchDelete) {
				sb.append("<td><input name=\"ids\" \" type=\"checkbox\" value=\"" + pkValue + "\"></td>");
			}

			for (MetaItem item : itemList) {
				// 是否显示字段
				boolean isShow = item.getBoolean("isShow");
				if (isShow) {
					String itemKey = item.getStr("en");
					sb.append("<td>" + record.get(itemKey).toString() + "</td>");
				}
			}
			sb.append("</tr>");
		}

		return sb.toString();
	}

	public static String getToolButton() {
		return "";

	}
}