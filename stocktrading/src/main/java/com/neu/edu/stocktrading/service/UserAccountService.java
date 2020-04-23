package com.neu.edu.stocktrading.service;

import com.neu.edu.stocktrading.dao.UserAccountDAO;
import com.neu.edu.stocktrading.model.User;
import com.neu.edu.stocktrading.model.UserBankDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService
{
    @Autowired
    private UserAccountDAO userAccountDAO;

    public User getProfileAttributes (String email)
    {
        return this.userAccountDAO.getProfileAttributes(email);
    }

    public User updateProfile (String  email , String phoneNumber)
    {
        return this.userAccountDAO.updateProfileAttributes(email , phoneNumber);
    }

    public User updateBankDetails (User user, UserBankDetails userbank)
    {
        return this.userAccountDAO.updateBankDetails(user , userbank);
    }

}