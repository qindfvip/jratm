
package com.base.beetl;

import org.beetl.core.Context;
import org.beetl.core.Function;

/**
 * 自定判断是否为True
 *
 */
public class IsTrueFun implements Function {
	public Object call(Object[] paras, Context ctx) {
		if (paras.length != 1) {
			throw new RuntimeException("参数错误，期望Object");
		}
		Object para = paras[0];
		if (para == null) {
			return false;
		}
		if (para.toString().equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}
}