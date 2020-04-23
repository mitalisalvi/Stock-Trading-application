package com.neu.edu.stocktrading.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.neu.edu.stocktrading.dao.StockAnalysisDAO;
import com.neu.edu.stocktrading.model.Stock;
import com.neu.edu.stocktrading.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockAnalysisService {
    @Autowired
    private StockAnalysisDAO stockAnalysisDAO;

    private static final Logger logger = LoggerFactory.getLogger(StockAnalysisService.class);

    public User getProfileAttributes(String email) {
        return this.stockAnalysisDAO.getProfileAttributes(email);
    }

    public Map<String, List<? extends Object>> retrieveSellList(User user) {
        return this.stockAnalysisDAO.retrieveSellList(user);
    }

    public ArrayList<Stock> removeDuplicates(List<Stock> list) {
        ArrayList<Stock> newList = new ArrayList<Stock>();

        for (Stock element : list) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }

        return newList;
    }

    public List<Stock> getPotentialBuyFromWatchlist(User user, List<Stock> sellStocks) {
        List<Stock> watchlistStocks = this.stockAnalysisDAO.getWatchlistStocks(user);

        Iterator itr = watchlistStocks.iterator();
        while (itr.hasNext()) {
            Stock s = (Stock) itr.next();
            if (sellStocks.contains(s)) {
                itr.remove();
            }
        }

        for (Stock s : watchlistStocks) {
            logger.info("potential buy list::" + s.getStockName());
        }

        return watchlistStocks;
    }

    public Map<String, List<Stock>> buildPortfolio(List<Stock> potentialBuyList, List<Stock> sellStocks, List<Integer> sellAmount) 
    {
        logger.info("buildPortfolio::INSIDE");
        List<Stock> combinedList = Stream.of(potentialBuyList, sellStocks).flatMap(x -> x.stream())
                .collect(Collectors.toList());

        logger.info("combinedList::"+combinedList.size());
        

        int totalSumStocks = 0;
        for (Integer e : sellAmount)
        {
            totalSumStocks += e;
        }
        logger.info("Total number of existing stocks::"+totalSumStocks);
        int totalNumberOfStocksInPortfolio = totalSumStocks * 2;

        int n = combinedList.size();
        int divideStocks = totalNumberOfStocksInPortfolio;

        List<Integer> divideRatio = new ArrayList<Integer>();

        Random random = new Random(System.currentTimeMillis());        
		for (int i = 0; i < n -1; i++) 
		{
           int j = random.nextInt(divideStocks - (n -i)) + 1;
           divideRatio.add(j);
		   divideStocks -= j;
        }
        divideRatio.add(divideStocks);
        
        logger.info("divideRatio::"+divideRatio);

        List<Stock> buyList = new ArrayList<Stock>();
        List<Stock> sellList = new ArrayList<Stock>();
        List<Stock> holdList = new ArrayList<Stock>();

        for (int i=0; i<divideRatio.size(); i++)
        {
            Stock s = combinedList.get(i);
            int amount =0;
            if (sellStocks.contains(s))
            {
                amount = getStockAmountFromStock(s, sellStocks, sellAmount);
                logger.info("selling amount::"+amount);
            }

            if (Integer.compare(divideRatio.get(i) , amount) > 0)
            {
                buyList.add(s);
            }
            else
            {
                sellList.add(s);
            }
        }

        logger.info("sellList:"+sellList);
        logger.info("buyList:"+buyList);

        if (sellList.size()>1)
        {
            Stock sellTemp = sellList.get(0);
            holdList.add(sellTemp);
            sellList.remove(sellTemp);
        }


        Map <String , List<Stock>> results = new HashMap<String , List<Stock>>();
        results.put("Sell", sellList);
        results.put("Buy", buyList);
        results.put("Hold", holdList);

        return results;

    }

    public int getStockAmountFromStock (Stock s, List<Stock> sellStocks, List<Integer> sellAmount)
    {
        int resultAmount =0;
        for (int i=0; i<sellStocks.size(); i++)
        {
            if (sellStocks.get(i).equals(s))
            {
                resultAmount = sellAmount.get(i);
                break;
            }
        }

        return resultAmount;

    }

}