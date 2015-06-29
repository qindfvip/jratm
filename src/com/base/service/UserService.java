package com.base.service;

import java.util.List;

import com.base.model.User;

public class UserService {

	public List<User> queryUser(String nickName){
		List<User> list = User.dao.queryUser(nickName);
		return list;
	}
	
	/**
	 * 根据账号获取用户
	 * @param loginId
	 * @return
	 */
	public User getUserByLoginId(String loginId) {
		return User.dao.getUserByLoginId(loginId);
	}
}