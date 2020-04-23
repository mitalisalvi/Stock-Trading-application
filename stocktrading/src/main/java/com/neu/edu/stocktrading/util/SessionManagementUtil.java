package com.neu.edu.stocktrading.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SessionManagementUtil
{
    private static final Logger logger = LoggerFactory.getLogger(SessionManagementUtil.class);

		public Boolean doesSessionExist(HttpServletRequest request) 
		{
			logger.debug("doesSessionExist::INSIDE");
			if (request.getSession(false) != null) 
			{
				logger.debug("Session exists");

				if (request.getSession().getAttribute("user") != null) 
				{
					logger.debug("User exists");
					return true;
				}
				else
				{
					logger.debug("User does not exists");
					return false;
				}
			}

		logger.debug("doesSessionExist::FALSE");
		return false;
	}

	public HttpSession createNewSessionForUser(HttpServletRequest request, String email) 
	{
		logger.debug("createNewSessionForUser::INSIDE::"+email);
		if (!doesSessionExist(request)) 
		{
			HttpSession session = request.getSession();
			session.setAttribute("user", email);
			return session;
		} 
		else 
		{
			logger.debug("createNewSessionForUser::Return current session::");
			return request.getSession();
		}
	}
}