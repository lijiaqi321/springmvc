package cn.smbms.dao.user;

import cn.smbms.pojo.User;


import java.sql.Connection;
import java.util.List;

public interface UserDao {


	/**
	 * 通过userCode获取User
	 * @param connection
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public User getLoginUser(Connection connection, String userCode)throws Exception;





}
