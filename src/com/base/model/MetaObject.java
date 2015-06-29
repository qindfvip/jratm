package com.base.model;

import com.base.common.base.BaseModel;
import com.base.common.utils.xx;

public class MetaObject extends BaseModel<MetaObject> {

	private static final long serialVersionUID = 1635722346260249629L;

	public static final MetaObject dao = new MetaObject();
	
	
	/**
	 * 获取数据源
	 * 
	 * @return
	 */
	public String getDs() {
		return this.getStr("dataSource");
	}

	/**
	 * 获取table
	 * 
	 * @return
	 */
	public String getTable() {
		return this.getStr("table");
	}

	/**
	 * 获取主键名
	 * 
	 * @return
	 */
	public String getPk() {
		return this.getStr("pkName");
	}

	/**
	 * 获取View(没有视图用Table)
	 * 
	 * @return
	 */
	public String getView() {
		String view = this.getStr("view");
		if (xx.isEmpty(view)) {
			view = getTable();
		}
		return view;
	}

	/**
	 * 根据Code获得对象
	 * @param code 对象Code
	 * @return 对象
	 */
	public MetaObject getByCode(String code) {
		return MetaObject.dao.findFirst("select * from "+getTableName()+" where code = ?", code);
	}
}