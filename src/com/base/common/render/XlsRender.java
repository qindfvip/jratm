package com.base.common.render;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.base.common.utils.excel.ExcelUtil;
import com.base.model.MetaItem;
import com.base.model.MetaObject;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

public class XlsRender extends Render {

	protected final Logger log = Logger.getLogger(getClass());
	private final static String CONTENT_TYPE = "application/msexcel;charset=" + getEncoding();

	private final MetaObject object;
	private final List<MetaItem> items;
	private final List<Record> data;

	private final String fileName;

	public XlsRender(List<Record> data, List<MetaItem> items, MetaObject object) {
		this.data = data;
		this.items = items;
		this.object = object;

		fileName = object.getView() + ".xls";
	}

	@Override
	public void render() {
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType(CONTENT_TYPE);
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			ExcelUtil.createExcel(os, data, items, object);
		} catch (Exception e) {
			throw new RenderException(e);
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}

		}
	}

}
