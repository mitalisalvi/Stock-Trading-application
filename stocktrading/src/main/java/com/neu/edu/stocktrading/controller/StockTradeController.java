package com.neu.edu.stocktrading.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.mysql.cj.Session;
import com.neu.edu.stocktrading.model.Trade;
import com.neu.edu.stocktrading.model.Transaction;
import com.neu.edu.stocktrading.model.User;
import com.neu.edu.stocktrading.service.StockTradeService;
import com.neu.edu.stocktrading.util.FileReaderUtil;
import com.neu.edu.stocktrading.util.GeneratePdfUtil;
import com.neu.edu.stocktrading.util.SessionManagementUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StockTradeController {
    private static final Logger logger = LoggerFactory.getLogger(StockTradeController.class);

    @Autowired
    private StockTradeService stockTradeService;

    @Autowired
    private SessionManagementUtil sessionMgmtUtils;
    
    @PostMapping(value = "/check/account.htm" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkIfAccountAttached (HttpServletRequest request , @RequestBody HashMap<String, String> stockSymbol)
    {
        logger.info("checkIfAccountAttached::");
        String email = (String) request.getSession().getAttribute("user");
        User user = this.stockTradeService.getProfileAttributes(email);

        boolean result = this.stockTradeService.checkIfAccountAttached(user);

        if (!result){
            return  ResponseEntity.badRequest().body("No user bank details found for user");
        }
        else {
            Map<String , String> res = new HashMap<String, String>();
            res.put("result", "Success");
            return ResponseEntity.ok(res);
        }

    }

    @PostMapping(value = "/trade/watchlist.htm")
    public ModelAndView addToTrade(HttpServletRequest request) {
        logger.info("addToTrade::");
        String email = (String) request.getSession().getAttribute("user");
        User user = this.stockTradeService.getProfileAttributes(email);

        String[] stockSymbols = request.getParameterValues("checkedRows");

        Map<String, List<? extends Object>> stockList = this.stockTradeService.retrieveBuyList(user, stockSymbols);
        ModelAndView mv = new ModelAndView();
        mv.addObject("stockList", stockList);
        mv.setViewName("user-trade");

        request.getSession().setAttribute("stockList", stockList);

        return mv;
    }

    @PostMapping(value = "/trade/transaction.htm")
    public ModelAndView showTradeTransaction(HttpServletRequest request) {
        logger.info("showTradeTransaction::");
        String email = (String) request.getSession().getAttribute("user");
        User user = this.stockTradeService.getProfileAttributes(email);

        String[] quantities = request.getParameterValues("quantity");

        String[] selling = request.getParameterValues("checkedRows");

        Map<String, List<? extends Object>> stockList = (Map<String, List<? extends Object>>) request.getSession()
                .getAttribute("stockList");

        List<? extends Object> stockBuying = stockList.get("Buy");

        ModelAndView mv = new ModelAndView();
        mv.setViewName("user-transaction");

        mv.addObject("transactionBuyId", new Random().nextInt(1000000));
        mv.addObject("transactionSellId", new Random().nextInt(1000000));

        Transaction t = this.stockTradeService.completeTransaction(user, quantities, stockBuying, selling);
        logger.info("showTradeTransaction::"+t.getId());

        List<Trade> currentBuyTrades = this.stockTradeService.getBuyTradesUsingCurrentTransaction (user , t);

        List<Trade> currentSellTrades = new ArrayList<Trade>();
        currentSellTrades = this.stockTradeService.getSellTradesUsingCurrentTransaction (user , t);
        


        mv.addObject("tradeBuyList", currentBuyTrades);
        mv.addObject("tradeSellList", currentSellTrades);

       


        Double serviceCharge = Double.parseDouble( FileReaderUtil.readServiceChargeValue() );
        Double buyTax = (serviceCharge/100 * this.stockTradeService.getTotalPrice(currentBuyTrades));
        mv.addObject("serviceBuyTax", buyTax);

        Double sellTax = (serviceCharge/100 * this.stockTradeService.getTotalPrice(currentSellTrades));
        mv.addObject("serviceSellTax", sellTax);

        mv.addObject("totalBuyPrice", this.stockTradeService.getTotalPrice(currentBuyTrades) + buyTax);
        mv.addObject("totalSellPrice", this.stockTradeService.getTotalPrice(currentSellTrades) + sellTax);

        return mv;
    }

    @GetMapping(value = "/statement.htm")
    public ModelAndView home(HttpServletRequest request) 
    {
        if (!this.sessionMgmtUtils.doesSessionExist(request)) 
        {
            logger.info("Please login to access this page");
            ModelAndView mv = new ModelAndView("login-form");
            mv.addObject("user", new User());
            mv.addObject("errorMessage", "Please login to access this page");
            return mv;

        }

        ModelAndView mv = new ModelAndView("account-statement");
        String email = (String) request.getSession().getAttribute("user");

        List<Transaction> temp = this.stockTradeService.getAllTransactions(email);

        List<Transaction> result = new ArrayList<Transaction>();
        if (temp !=null)
        {
            for (Transaction t : temp)
            {
                if (Double.compare(t.getTotalPrice(), 0.0) !=0)
                {
                    result.add(t);
                }
            }
        }

        mv.addObject("transactionList", result);
        return mv;
    }

    @GetMapping(value = "/transaction.pdf" , produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePdf(HttpServletRequest request) 
    {
        String email = (String) request.getSession().getAttribute("user");
        List<Transaction> temp = this.stockTradeService.getAllTransactions(email);
        List<Transaction> result = new ArrayList<Transaction>();
        if (temp !=null)
        {
            for (Transaction t : temp)
            {
                if (Double.compare(t.getTotalPrice(), 0.0) !=0)
                {
                    result.add(t);
                }
            }
        }

        ByteArrayInputStream bis = GeneratePdfUtil.transactionReport(result);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=transactionReport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

}