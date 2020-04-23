package com.neu.edu.stocktrading.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.edu.stocktrading.dao.HomePageDAO;
import com.neu.edu.stocktrading.model.Stock;
import com.neu.edu.stocktrading.model.StockAPIBean;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.sf.json.JSONSerializer;

@Service
public class HomePageService
{
    @Autowired
    private HomePageDAO homePageDAO;

    private static final Logger logger = LoggerFactory.getLogger(HomePageService.class);

    @Autowired
    private RestTemplate restTemplate;

    // final String stockUri = "https://marketdata.websol.barchart.com/getQuote.json?apikey=b0ccbbb0dae9d39ce81f65718f0ecd10&symbols=AMZN,GOOG,AAPL,GOOG,NFLX,TSLA,FB,CSCO,ORCL,INTC,QCOM,EBAY,DELL,COST,MSFT,TWTR,AABA,SNAP,AMD,ATVI,ZNGA,WDC,BKNG&fields=fiftyTwoWkHigh%2CfiftyTwoWkHighDate%2CfiftyTwoWkLow%2CfiftyTwoWkLowDate";
    final String stockUri1 = "https://marketdata.websol.barchart.com/getQuote.json?apikey=b0ccbbb0dae9d39ce81f65718f0ecd10&symbols=AMZN,GOOG,AAPL,NFLX,TSLA,FB,CSCO,ORCL,INTC,QCOM,EBAY,DELL,COST,MSFT,TWTR,AABA,SNAP,AMD,ATVI,ZNGA&fields=fiftyTwoWkHigh%2CfiftyTwoWkHighDate%2CfiftyTwoWkLow%2CfiftyTwoWkLowDate";

    final String stockUri2 = "https://marketdata.websol.barchart.com/getQuote.json?apikey=b0ccbbb0dae9d39ce81f65718f0ecd10&symbols=WDC,BKNG,VZ,HPQ,SNE,W,BABA,JNJ,JPM,XOM,BAC,WMT,WFC,V,PG,BUD,T,UNH,HD,C&fields=fiftyTwoWkHigh%2CfiftyTwoWkHighDate%2CfiftyTwoWkLow%2CfiftyTwoWkLowDate";



    final String currencyUri = "https://www.worldtradingdata.com/api/v1/forex?base=USD&sort=newest&api_token=uRqeU8C651htqmF8iI5N6VvSBEIpTiQ5xvvKkz9Slsm1D9jFvIGdG97m0RpF";

    public List<StockAPIBean> getTopStocks ()
    {
        List<StockAPIBean> stocks = new ArrayList<StockAPIBean>();

        String result = this.restTemplate.getForObject(stockUri1, String.class);
        Object obj = null;
        JSONObject jo = null;
        try {
            obj = new JSONParser().parse(result);
            jo = (JSONObject) obj;
        } catch (ParseException e) {
            logger.error(e.toString());
            obj = null;
            jo = null;
        }

        if (obj != null && jo != null) {
            JSONArray ja = (JSONArray) jo.get("results");

            Iterator itr2 = ja.iterator();

            while (itr2.hasNext()) {

                Map map = (Map) itr2.next();
                ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                StockAPIBean pojo = mapper.convertValue(map, StockAPIBean.class);
                stocks.add(pojo);
            }
        }

        result = this.restTemplate.getForObject(stockUri2, String.class);
        obj = null;
        jo = null;
        try {
            obj = new JSONParser().parse(result);
            jo = (JSONObject) obj;
        } catch (ParseException e) {
            logger.error(e.toString());
            obj = null;
            jo = null;
        }

        if (obj != null && jo != null) 
        {
            JSONArray ja = (JSONArray) jo.get("results");

            Iterator itr2 = ja.iterator();

            while (itr2.hasNext()) {

                Map map = (Map) itr2.next();
                ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                StockAPIBean pojo = mapper.convertValue(map, StockAPIBean.class);
                stocks.add(pojo);
            }
        }

        saveAllStocks(stocks);

        logger.info("size::" + stocks.size());
        return stocks;

    }

    public void saveAllStocks(List<StockAPIBean> stocks)
    {

        logger.info("Saving stock object to database");
        for (StockAPIBean stock : stocks) {
            Stock temp = null;
            try {
                temp = this.homePageDAO.checkIfStockExists(stock.getSymbol());
            } catch (Exception e) {
                temp = null;
            }
            if (temp == null) 
            {
                //this.homePageDAO.saveStock(stock);
            } 
            else {
                this.homePageDAO.updateStock(stock, temp.getId());

            }

        }
        logger.info("Done updating stock table");

    }

    public Map<String , String> getTopCurrencies ()
    {
        String result = this.restTemplate.getForObject(currencyUri, String.class);

        net.sf.json.JSONObject json = (net.sf.json.JSONObject) JSONSerializer.toJSON( result );        
        net.sf.json.JSONObject data = json.getJSONObject("data");
        
        Map<String,String> map = new HashMap<String,String>();
        Iterator iter = data.keys();
        
        while(iter.hasNext())
        {
            String key = (String)iter.next();
            String value = data.getString(key);
            map.put(key,value);
        }

        Map<String,String> resultMap = new HashMap<String,String>();

        String eur = map.get("EUR");
        String jpy = map.get("JPY");
        String inr = map.get("INR");

        resultMap.put("Euro", eur);
        resultMap.put("Yen", jpy);
        resultMap.put("Rupee", inr);

        return resultMap;
        
    }

    

}