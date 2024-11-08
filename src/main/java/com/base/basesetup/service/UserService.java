package com.base.basesetup.service;

import org.springframework.stereotype.Service;

@Service
public interface UserService {

	public void removeUser(String userName);
	public void createUserAction(String userName, long userId, String actionType);

}
