package com.neu.edu.stocktrading.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neu.edu.stocktrading.dao.ForecastDAO;
import com.neu.edu.stocktrading.model.Stock;
import com.neu.edu.stocktrading.model.User;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ForecastService {
    private static final Logger logger = LoggerFactory.getLogger(ForecastDAO.class);

    @Autowired
    private ForecastDAO forecastDAO;

    @Autowired
    private RestTemplate restTemplate;


    public User getProfileAttributes (String email)
    {
        return this.forecastDAO.getProfileAttributes(email);
    }

    public List<Stock> getRemmendations(List<Stock> sellingList) {

        List<Stock> stocks = new ArrayList<Stock>();

        List<String> symList = new ArrayList<String>();

        for (Stock s : sellingList) 
        {
            String symbol = s.getStockSymbol();
            String uri = "https://api.iextrading.com/1.0/stock/" + symbol + "/peers";
            String result = this.restTemplate.getForObject(uri, String.class);
           
            if (result !=null) 
            {
                try {
                    JSONArray json = new JSONArray(result);
                    for (int i = 0; i < json.length(); i++) 
                    {
                        symList.add(json.getString(i));
                    }

                } catch (Exception e) {
                    logger.error(e.toString());
                }
            }
        }

        logger.info("Done adding recommended stocks from api");

        for (String sym : symList)
        {
            Stock temp = null;
            try {
                temp = this.forecastDAO.checkIfStockExists(sym);
            } catch (Exception e) 
            {
                temp = null;
            }

            if (temp !=null)
            {
                stocks.add(temp);
            }
          
        }

        logger.info("Recommendation list::"+ stocks.size());

        return stocks;

    }

    public Map<String , List<? extends Object>> retrieveSellList ( User user)
    {
        return this.forecastDAO.retrieveSellList(user);
    }

    public boolean addStockToWatchList(User user, String stockSymbol) {
        return this.forecastDAO.addStockToWatchList(user, stockSymbol);
    }

    public ArrayList<Stock> removeDuplicates(List<Stock> list) 
    { 
  
        ArrayList<Stock> newList = new ArrayList<Stock>(); 
  
        for (Stock element : list) { 
  
            if (!newList.contains(element)) 
            { 
                newList.add(element); 
            } 
        } 
  
        return newList; 
    }
}