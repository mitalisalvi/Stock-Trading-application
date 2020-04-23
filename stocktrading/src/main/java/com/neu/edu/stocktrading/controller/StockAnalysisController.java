package com.neu.edu.stocktrading.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.neu.edu.stocktrading.model.Stock;
import com.neu.edu.stocktrading.model.User;
import com.neu.edu.stocktrading.service.StockAnalysisService;
import com.neu.edu.stocktrading.util.SessionManagementUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StockAnalysisController
{
    @Autowired
    private StockAnalysisService stockAnalysisService;

    @Autowired
    private SessionManagementUtil sessionMgmtUtils;

    private static final Logger logger = LoggerFactory.getLogger(StockAnalysisController.class);

    @GetMapping(value = "/analysis.htm" )
    public ModelAndView buildUserPortfolio (HttpServletRequest request )
    {
        logger.info("buildUserPortfolio::");

        if (!this.sessionMgmtUtils.doesSessionExist(request)) 
        {
            logger.info("Please login to access this page");
            ModelAndView mv = new ModelAndView("login-form");
            mv.addObject("user", new User());
            mv.addObject("errorMessage", "Please login to access this page");
            return mv;

        }

        String email = (String) request.getSession().getAttribute("user");
        User user = this.stockAnalysisService.getProfileAttributes(email);

        Map<String, List<? extends Object>> stockList = this.stockAnalysisService.retrieveSellList(user);
        List<Stock> sellStocks = (List<Stock>) stockList.get("Sell");
        List<Integer> sellAmount = (List<Integer>) stockList.get("Amount");

        List<Stock> potentialBuyList = this.stockAnalysisService.getPotentialBuyFromWatchlist(user, sellStocks);

        Map<String, List<Stock>> results = new HashMap<String, List<Stock>>();

        if (sellStocks.size()!=0) {
            results =this.stockAnalysisService.buildPortfolio(potentialBuyList, sellStocks, sellAmount);
        }
        
        ModelAndView mv = new ModelAndView();
        mv.addObject("analysisMap", results);
        mv.setViewName("stock-analysis");
        return mv;
    }


}