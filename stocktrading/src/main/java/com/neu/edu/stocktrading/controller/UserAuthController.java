package com.neu.edu.stocktrading.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neu.edu.stocktrading.model.LoginForm;
import com.neu.edu.stocktrading.model.User;
import com.neu.edu.stocktrading.service.UserAuthService;
import com.neu.edu.stocktrading.util.SessionManagementUtil;
import com.neu.edu.stocktrading.validator.RegisterUserValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserAuthController {
    private static final Logger logger = LoggerFactory.getLogger(UserAuthController.class);

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private SessionManagementUtil sessionMgmtUtils;

    @Autowired
    RegisterUserValidator registerUserValidator;

    @GetMapping(value = "/register.htm")
    public ModelAndView showRegisterPage(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        ModelAndView mv = new ModelAndView("register-form");
        mv.addObject("user", new User());
        return mv;
    }

    @PostMapping(value = "/register.htm")
    public ModelAndView registerUser(HttpServletRequest request,
            HttpServletResponse response, @ModelAttribute("user") @Validated User user, BindingResult bindingResult,
            ModelMap map) throws IllegalStateException, IOException {

        registerUserValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
        {
            ModelAndView mv = new ModelAndView("register-form");
            mv.addObject("user", new User());
            mv.addObject("errorMessage", "Password should be minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character");
            return mv;
        }

        if (this.userAuthService.checkIfUserRegistered(user)) {
            ModelAndView mv = new ModelAndView("register-form");
            mv.addObject("user", new User());
            mv.addObject("errorMessage", "Oops. An account with this email already exists.");
            return mv;
        }

        if (userAuthService.registerUser(user)) 
        {
            logger.info("User successfully registered");
            ModelAndView mv = new ModelAndView("login-form");
            mv.addObject("user", new User());
            return mv;
        } 
        else 
        {
            ModelAndView mv = new ModelAndView("register-form");
            mv.addObject("user", new User());
            mv.addObject("errorMessage", "Oops. Something went wrong. Please try again.");
            return mv;

        }

    }

    @GetMapping(value = "/login.htm")
    public ModelAndView showLoginPage(HttpServletRequest request, HttpServletResponse response, LoginForm user) {
        ModelAndView mv = new ModelAndView("login-form");
        mv.addObject("user", new User());
        return mv;
    }

    @PostMapping(value = "/login.htm")
    public ModelAndView loginUser(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute("user") @Validated LoginForm loginForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("login-form");
        }

        if (!this.sessionMgmtUtils.doesSessionExist(request)) {

            if (this.userAuthService.isAdmin(loginForm) == true) {
                logger.info("Admin login");
                this.sessionMgmtUtils.createNewSessionForUser(request, loginForm.getEmail());
                ModelAndView mv = new ModelAndView("redirect:/");
                request.getSession().setAttribute("isAdmin", "Yes");
                return mv;
            } else if (this.userAuthService.authenticate(loginForm) == true) {
                logger.info("Successfully authenticated");
                this.sessionMgmtUtils.createNewSessionForUser(request, loginForm.getEmail());
                ModelAndView mv = new ModelAndView("redirect:/");
                if (this.userAuthService.isUserVerified(loginForm)) {
                    request.getSession().setAttribute("verified", "yes");
                } else {
                    request.getSession().setAttribute("verified", "no");
                }
                return mv;

            } else {
                logger.info("Invalid user credentails");
                ModelAndView mv = new ModelAndView("login-form");
                mv.addObject("user", new User());
                mv.addObject("errorMessage", "Invalid credentails. Please try again.");
                return mv;
            }

        }
        ModelAndView mv = new ModelAndView("redirect:/");
        return mv;

    }

    @RequestMapping(value = "/logout.htm")
    public ModelAndView logoutUser(HttpServletRequest request) 
    {
        logger.info("Logging out");
        request.getSession().invalidate();
        return new ModelAndView("redirect:/");
    }

}