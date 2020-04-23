package com.neu.edu.stocktrading.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neu.edu.stocktrading.model.User;
import com.neu.edu.stocktrading.model.UserBankDetails;
import com.neu.edu.stocktrading.service.UserAccountService;
import com.neu.edu.stocktrading.util.SessionManagementUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserAccountController
{
    private static final Logger logger = LoggerFactory.getLogger(UserAccountController.class);
    
    @Autowired
    private SessionManagementUtil sessionMgmtUtils;

    @Autowired
    private UserAccountService userAccountService;


    @GetMapping(value = "/update/profile.htm")
    public ModelAndView updateProfilePage(HttpServletRequest request) 
    {
        if (!this.sessionMgmtUtils.doesSessionExist(request)) 
        {
            logger.info("Please login to access this page");
            ModelAndView mv = new ModelAndView("login-form");
            mv.addObject("user", new User());
            mv.addObject("errorMessage", "Please login to access this page");
            return mv;

        }
        ModelAndView mv = new ModelAndView("profile-update");
        String email = (String) request.getSession().getAttribute("user");
        User user = this.userAccountService.getProfileAttributes(email);
        logger.info("updateProfilePage::"+user.toString());
        mv.addObject("user", user);
        return mv;
    }


    @PostMapping(value = "/update/profile.htm")
    public ModelAndView updateProfile( HttpServletRequest request,
            HttpServletResponse response, @ModelAttribute("user") @Validated User user ,  BindingResult bindingResult) throws IllegalStateException, IOException {

        logger.info("Inside updateProfile::"+user.toString());
        User updatedUser = this.userAccountService.updateProfile(user.getEmail() , user.getPhoneNumber());

        ModelAndView mv = new ModelAndView("profile-update");
        mv.addObject("user", updatedUser);
        mv.addObject("successMessage", "Profile updated successfully");
        return mv;
    }

    @GetMapping(value = "/update/bankAccount.htm")
    public ModelAndView manageBankAccount(HttpServletRequest request) 
    {
        if (!this.sessionMgmtUtils.doesSessionExist(request)) 
        {
            logger.info("Please login to access this page");
            ModelAndView mv = new ModelAndView("login-form");
            mv.addObject("user", new User());
            mv.addObject("errorMessage", "Please login to access this page");
            return mv;

        }
        ModelAndView mv = new ModelAndView("user-account");

        String email = (String) request.getSession().getAttribute("user");
        User user = this.userAccountService.getProfileAttributes(email);
        UserBankDetails userBank = user.getUserBankDetails();
        if (userBank ==null)
            userBank = new UserBankDetails();
            
        logger.info(userBank.toString());
        mv.addObject("userBank", userBank);
        return mv;
    }

    @PostMapping(value = "/update/bankAccount.htm")
    public ModelAndView updateBankDetails( HttpServletRequest request,
            HttpServletResponse response, @ModelAttribute("userBank") @Validated UserBankDetails userbank ,  BindingResult bindingResult
          ) throws IllegalStateException, IOException {
                
        logger.info("Inside updateBankDetails::"+userbank.toString());

        String email = (String) request.getSession().getAttribute("user");
        User user = this.userAccountService.getProfileAttributes(email);

        logger.info(userbank.toString());

        User updatedUser = this.userAccountService.updateBankDetails(user,userbank);

        ModelAndView mv = new ModelAndView("user-account");
        mv.addObject("user", updatedUser);
        mv.addObject("successMessage", "Bank Account added successfully");
        return mv;
    }
}