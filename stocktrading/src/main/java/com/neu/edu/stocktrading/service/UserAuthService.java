package com.neu.edu.stocktrading.service;

import java.io.IOException;


import com.neu.edu.stocktrading.dao.UserAuthDAO;
import com.neu.edu.stocktrading.model.LoginForm;
import com.neu.edu.stocktrading.model.User;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {
	private static final Logger logger = LoggerFactory.getLogger(UserAuthService.class);

	@Autowired
	private UserAuthDAO userAuthDAO;

	@Value("${admin.email}")
	private String adminEmail;

	@Value("${admin.password}")
	private String adminPassword;

	public boolean registerUser(User user) throws IllegalStateException, IOException 
	{
		user.setPassword(BCrypt.hashpw( user.getPassword(), BCrypt.gensalt()));
		this.userAuthDAO.saveUser(user);
		return true;
	}

	public boolean checkIfUserRegistered (User user)
	{
		logger.info("Checking if user exists ::" + user.getEmail());
		int result = this.userAuthDAO.checkIfUserExists(user.getEmail());
		if (result >0) {
			return true;
		}
		else 
			return false;

	}

	public Boolean authenticate(LoginForm u) 
	{
		logger.info("Checking if user exists on the basis of email::" + u.getEmail());

		User user = null;
		try {
			user = this.userAuthDAO.getProfileAttributes(u.getEmail());
		} catch (Exception e) {
			logger.error(e.toString());
			user = null;
		}

		if (user == null) {
			return false;
		} else {
			Boolean pwdCheck = false;

			if (BCrypt.checkpw(u.getPassword(), user.getPassword())) {
				pwdCheck = true;
			}
			return pwdCheck;

		}

	}

	public boolean isAdmin (LoginForm loginForm)
	{
		if (loginForm.getEmail().equals(adminEmail))
		{
			if (loginForm.getPassword().equals(adminPassword))
			{
				return true;
			}
			else
				return false;
		}
		else
			return false;

	}

	public boolean isUserVerified (LoginForm loginForm)
	{
		User user = this.userAuthDAO.getProfileAttributes(loginForm.getEmail());
		if ( user.isVerified())
		{
			return true;
		}
		else
		{
			return false;
		}

	}
}