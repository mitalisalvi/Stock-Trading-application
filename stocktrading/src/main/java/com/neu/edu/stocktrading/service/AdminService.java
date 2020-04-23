package com.neu.edu.stocktrading.service;

import java.util.List;

import com.neu.edu.stocktrading.dao.AdminDAO;
import com.neu.edu.stocktrading.model.User;
import com.neu.edu.stocktrading.util.FileReaderUtil;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService
{
    @Autowired
    private AdminDAO adminDAO;

    private static final Logger logger = LoggerFactory.getLogger(AdminDAO.class);

    public void updateServiceCharge (String val)
    {
        FileReaderUtil.writeServiceChargeValue(val);
    }

    public List<User> getUnverifiedUsers ()
    {
        return this.adminDAO.getUnverifiedUsers();
    }

    public boolean sendEmail (String[] emails)
    {
        for (String em : emails)
        {
            try {
                sendEmail(em);
                this.adminDAO.verifyUsers(em);
            } catch (Exception e) 
            {
                logger.error(e.toString());
                return false;
            }
        }

        for (String em : emails)
        {
            User u  = this.adminDAO.getProfileAttributes(em);
            logger.info("verification status::"+u.isVerified());

        }
        return true;


    }

    private void sendEmail (String emailId) throws EmailException
	{
		Email email  =new SimpleEmail();
		email.setHostName("smtp.googlemail.com");
        // email.setSmtpPort(465);
        email.setSmtpPort(587);
		
		email.setAuthenticator(new DefaultAuthenticator("stocktrader6250@gmail.com", "Pass@123"));
		email.setSSLOnConnect(true);
		email.setFrom("stocktrader6250@gmail.com");
		email.setSubject("Thank you for signing up on Stock Trading ");
		email.setMsg("This email is being sent you to notify that your account has now been verified");
		email.addTo(emailId);
		email.send();
	}

}