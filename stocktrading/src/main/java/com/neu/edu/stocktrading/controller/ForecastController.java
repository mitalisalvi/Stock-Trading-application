package com.neu.edu.stocktrading.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.neu.edu.stocktrading.model.Stock;
import com.neu.edu.stocktrading.model.User;
import com.neu.edu.stocktrading.service.ForecastService;
import com.neu.edu.stocktrading.util.SessionManagementUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ForecastController
{
    private static final Logger logger = LoggerFactory.getLogger(ForecastController.class);

    @Autowired
    private ForecastService forecastService;

    @Autowired
    private SessionManagementUtil sessionMgmtUtils;

    @GetMapping(value = "/forecast.htm" )
    public ModelAndView getRecommendations (HttpServletRequest request )
    {
        logger.info("getRecommendations::");
        if (!this.sessionMgmtUtils.doesSessionExist(request)) 
        {
            logger.info("Please login to access this page");
            ModelAndView mv = new ModelAndView("login-form");
            mv.addObject("user", new User());
            mv.addObject("errorMessage", "Please login to access this page");
            return mv;

        }

        String email = (String) request.getSession().getAttribute("user");
        User user = this.forecastService.getProfileAttributes(email);

        Map<String, List<? extends Object>> stockList = this.forecastService.retrieveSellList(user);
        List<Stock> sellStocks = (List<Stock>) stockList.get("Sell");

        List<Stock> recos = new ArrayList<Stock>();

        if (sellStocks.size()!=0)
        {
            recos =this.forecastService.getRemmendations(sellStocks);
            recos = this.forecastService.removeDuplicates(recos);
        }
        
        ModelAndView mv = new ModelAndView();
        mv.addObject("stockList", recos);
        mv.setViewName("stock-forecast");
        return mv;
    }

    @PostMapping(value = "/forecast.htm" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addToWatchList (HttpServletRequest request , @RequestBody HashMap<String, String> stockSymbol)
    {
        logger.info("addToWatchList::"+stockSymbol.get("symbol"));
        String email = (String) request.getSession().getAttribute("user");
        User user = this.forecastService.getProfileAttributes(email);


        boolean result = this.forecastService.addStockToWatchList (user , stockSymbol.get("symbol"));
        Map<String , String> res = new HashMap<String, String>();
        res.put("result", "Success");
        return ResponseEntity.ok(res);
    }

}