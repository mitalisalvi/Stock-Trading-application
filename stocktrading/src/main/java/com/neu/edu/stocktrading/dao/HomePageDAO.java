package com.neu.edu.stocktrading.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.neu.edu.stocktrading.model.Stock;
import com.neu.edu.stocktrading.model.StockAPIBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HomePageDAO {
    private static final Logger logger = LoggerFactory.getLogger(HomePageDAO.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void saveStock(StockAPIBean stock) 
    {
        Stock st = new Stock();
        st.setStockName(stock.getName());
        st.setStockSymbol(stock.getSymbol());
        st.setChange(stock.getPercentChange());

        Double tempDouble = 0.0;
        if (stock.getLow() != null && !stock.getLow().equals("")) {
            st.setBuyingPrice(Double.parseDouble(stock.getLow()));
        } else
            st.setBuyingPrice(tempDouble);

        if (stock.getHigh() != null && !stock.getHigh().equals("")) {
            st.setSellingPrice(Double.parseDouble(stock.getHigh()));
        } else
            st.setSellingPrice(tempDouble);

        if (stock.getFiftyTwoWkHigh() != null && !stock.getFiftyTwoWkHigh().equals("")) {
            st.setHigh52(Double.parseDouble(stock.getFiftyTwoWkHigh()));
        } else
            st.setHigh52(tempDouble);

        if (stock.getFiftyTwoWkLow() != null && !stock.getFiftyTwoWkLow().equals("")) {
            st.setLow52(Double.parseDouble(stock.getFiftyTwoWkLow()));
        } else
            st.setLow52(tempDouble);

        if (stock.getLastPrice() != null && !stock.getLastPrice().equals("")) {
            st.setCurrentPrice(Double.parseDouble(stock.getLastPrice()));
        } else
            st.setCurrentPrice(tempDouble);

        st.setMarketType(stock.getExchange());

        this.entityManager.merge(st);
        flushAndClear();

    }

    @Transactional
    public void updateStock(StockAPIBean stock, Long id) 
    {
        Stock st = this.entityManager.find(Stock.class, id);
        st.setStockName(stock.getName());
        st.setStockSymbol(stock.getSymbol());

        st.setChange(stock.getPercentChange());

        Double tempDouble = 0.0;
        // if (stock.getLow() != null && !stock.getLow().equals("")) {
        //     st.setBuyingPrice(Double.parseDouble(stock.getLow()));
        // } else
        //     st.setBuyingPrice(tempDouble);

        // if (stock.getHigh() != null && !stock.getHigh().equals("")) {
        //     st.setSellingPrice(Double.parseDouble(stock.getHigh()));
        // } else
        //     st.setSellingPrice(tempDouble);


        if (stock.getLow() != null && !stock.getLow().equals("")) {
            st.setBuyingPrice(Double.parseDouble(stock.getLastPrice()));
        } else
            st.setBuyingPrice(tempDouble);

        if (stock.getHigh() != null && !stock.getHigh().equals("")) {
            st.setSellingPrice(Double.parseDouble(stock.getLastPrice()));
        } else
            st.setSellingPrice(tempDouble);

        if (stock.getFiftyTwoWkHigh() != null && !stock.getFiftyTwoWkHigh().equals("")) {
            st.setHigh52(Double.parseDouble(stock.getFiftyTwoWkHigh()));
        } else
            st.setHigh52(tempDouble);

        if (stock.getFiftyTwoWkLow() != null && !stock.getFiftyTwoWkLow().equals("")) {
            st.setLow52(Double.parseDouble(stock.getFiftyTwoWkLow()));
        } else
            st.setLow52(tempDouble);

        if (stock.getLastPrice() != null && !stock.getLastPrice().equals("")) {
            st.setCurrentPrice(Double.parseDouble(stock.getLastPrice()));
        } else
            st.setCurrentPrice(tempDouble);

        st.setMarketType(stock.getExchange());
        flushAndClear();

    }

    public Stock checkIfStockExists(String symbol) throws NoResultException 
    {
        logger.info("Getting user data from database for user ");
        TypedQuery<Stock> query = this.entityManager.createQuery("SELECT c from Stock c where c.stockSymbol = ?1",
                Stock.class);
        query.setParameter(1, symbol);
        query.setMaxResults(1);
        return query.getSingleResult();
    }

    private void flushAndClear() {
        this.entityManager.flush();
        this.entityManager.clear();
    }

}