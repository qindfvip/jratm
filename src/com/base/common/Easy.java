package com.base.common;

/**
 * {"msg":"主键不可为空！","success":false}
 *
 */
public class Easy {
	
	/**处理结果**/
	private boolean success = true;
	private String msg = "操作成功";
	
	/**
	 * 默认操作成功
	 */
	public Easy(){
		
	}
	
	/**
	 * 操作失败构造
	 * @param msg
	 */
	public Easy(String msg){
		this.msg = msg;
		this.success = false;
	}
	
	/**
	 * 自定义构造
	 * @param msg
	 * @param status
	 */
	public Easy(String msg, boolean status){
		this.msg = msg;
		this.success = status;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}