package cn.smbms.service.user;

import cn.smbms.pojo.User;

import java.util.List;

public interface UserService {


	/**
	 * 用户登录
	 * @param userCode
	 * @param userPassword
	 * @return
	 */
	public User login(String userCode, String userPassword);


}
