package com.base.template.common.vo;

/**
 * 菜单URL命令处理类
 * 
 */
public class UrlCmd {

	/**
	 * URL初始化 objectCode=xxx
	 * 
	 * @param url
	 */
	public UrlCmd(String url) {
		init(url);
	}
	
	private String menuCode;
	private String objectCode;

	private void init(String url) {
		String[] cmds = url.split("&");
		for (String cmd : cmds) {
			String key = cmd.substring(0, cmd.indexOf("="));
			String value = cmd.substring(cmd.indexOf("=") + 1, cmd.length());
			if (key.equals("menuCode")) {
				this.setMenuCode(value);
				continue;
			}
			if (key.equals("objectCode")) {
				this.setObjectCode(value);
				continue;
			}
		}
	}

	public static void main(String[] args) {
		UrlCmd cmd = new UrlCmd("menuCode=biz_game_list&objectCode=game_code");
		System.out.println(cmd.getObjectCode());
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getObjectCode() {
		return objectCode;
	}

	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
}