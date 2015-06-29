
package com.base.beetl;

import org.beetl.core.Context;
import org.beetl.core.Function;

import com.base.common.utils.xx;

/**
 * JS 参数自动获取并处理默认值
 * 
 */
public class JsFormatFun implements Function {
	@Override
	public Object call(Object[] paras, Context ctx) {
		if (paras.length != 1) {
			throw new RuntimeException("参数错误，期望Object");
		}
		Object obj = paras[0];
		if (xx.isEmpty(obj)) {
			return "undefined";
		}
		if (xx.isNum(obj)) {
			return obj.toString();
		}
		return xx.format(obj);
	}
}