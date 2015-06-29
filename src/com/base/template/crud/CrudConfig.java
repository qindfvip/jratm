package com.base.template.crud;

import com.jfinal.core.Controller;

/**
 * CRUD模板 配置
 * 
 * 
 */
public class CrudConfig extends Controller {

	/** CRUD控制器URL路由名 **/
	public static String contro = "singleGrid";

	/** 新增处理方法名 **/
	public static final String ADD = "add";
	/** 删除处理方法名 **/
	public static final String DELETE = "delete";
	/** 更新处理方法名 **/
	public static final String UPDATE = "update";
	/** 查询处理方法名 **/
	public static final String LIST = "list";
	/** 导入处理方法名 **/
	public static final String IMPORTXLS = "importXls";
}