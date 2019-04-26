package com.onlineMIS.action.headQ.user;

import java.util.ArrayList;
import java.util.List;

import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;

public class UserActionUIBean {

	protected List<UserInfor> users = new ArrayList<UserInfor>();
	public List<UserInfor> getUsers() {
		return users;
	}

	public void setUsers(List<UserInfor> users) {
		this.users = users;
	}
}
