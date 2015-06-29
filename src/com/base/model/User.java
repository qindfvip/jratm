package com.base.model;

import java.util.List;

import com.base.common.base.BaseModel;

public class User extends BaseModel<User> {

	private static final long serialVersionUID = 1064291771401662738L;

	public static final User dao = new User();
	
	
	public List<User> queryUser(String nickName){
		List<User> list = User.dao.find("select * from "+getTableName()+" where nickName = ?", nickName);
		return list;
	}
	
	/**
	 * 根据账号获取用户
	 * @param loginId
	 * @return
	 */
	public User getUserByLoginId(String loginId) {
		return User.dao.findFirst("select * from "+getTableName()+"  where loginId = ?", loginId);
	}
	
}