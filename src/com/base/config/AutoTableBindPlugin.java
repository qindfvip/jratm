package com.base.config;

import java.util.List;

import javax.sql.DataSource;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;

/***
 * 自动绑定model与数据库表
 */
public class AutoTableBindPlugin extends ActiveRecordPlugin {
	private TableNameStyle tableNameStyle;

	public AutoTableBindPlugin(DataSource dataSource) {
		super(dataSource);
	}

	public AutoTableBindPlugin(String configName,IDataSourceProvider dataSourceProvider, TableNameStyle tableNameStyle) {
		super(configName,dataSourceProvider);
		this.tableNameStyle = tableNameStyle;
	}
	
	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, TableNameStyle tableNameStyle) {
		super(dataSourceProvider);
		this.tableNameStyle = tableNameStyle;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean start() {
		try {
			List<Class> modelClasses = ClassSearcher.findClasses(Model.class);
			TableBind tb = null;
			for (Class modelClass : modelClasses) {
				tb = (TableBind) modelClass.getAnnotation(TableBind.class);
				if (tb == null) {
					continue;
					//this.addMapping(tableName(modelClass), modelClass);
				} else {
					if (StrKit.notBlank(tb.name())) {
						if (StrKit.notBlank(tb.pk())) {
							this.addMapping(tb.name(), tb.pk(), modelClass);
						} else {
							this.addMapping(tb.name(), modelClass);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.start();
	}

	@Override
	public boolean stop() {
		return super.stop();
	}

	@SuppressWarnings("unused")
	private String tableName(Class<?> clazz) {
		String tableName = clazz.getSimpleName();
		if (tableNameStyle == TableNameStyle.UP) {
			tableName = tableName.toUpperCase();
		} else if (tableNameStyle == TableNameStyle.LOWER) {
			tableName = tableName.toLowerCase();
		} else {
			tableName = StrKit.firstCharToLowerCase(tableName);
		}
		return tableName;
	}
}
