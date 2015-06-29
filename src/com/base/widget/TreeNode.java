
package com.base.widget;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

/**
 * Tree Node VO
 *
 */
public class TreeNode extends Record {

	private static final long serialVersionUID = -5190761342805087001L;
	
	// 子节点
	private List<TreeNode> childList;

	public List<TreeNode> getChildList() {
		return childList;
	}

	public void setChildList(List<TreeNode> childList) {
		this.childList = childList;
	}

}