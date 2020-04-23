package com.neu.edu.stocktrading.dao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.neu.edu.stocktrading.model.Stock;
import com.neu.edu.stocktrading.model.User;
import com.neu.edu.stocktrading.model.WatchList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WatchListDAO
{
    @PersistenceContext
	private EntityManager entityManager;

	private static final Logger logger = LoggerFactory.getLogger(WatchListDAO.class);

    public User getProfileAttributes(String email) {
		logger.info("Getting user from email : " + email);
		TypedQuery<User> query = this.entityManager.createQuery("SELECT u from User u where u.email = ?1", User.class);
        query.setParameter(1, email);
        query.setMaxResults(1);
		return query.getSingleResult();
    }
    
    @Transactional
    public boolean addStockToWatchList(User user, String stockSymbol) 
    {
        logger.info("addStockToWatchList::"+stockSymbol);
        logger.info("addStockToWatchList::"+user.getFirstName());
        Stock st = null;
        try {
            st=checkIfStockExists(stockSymbol);
        } catch (Exception e) {
            st=null;
        }

        if (st!=null)
        {
            WatchList wt = user.getWatchList();
            
            if (wt ==null)
            {
                wt = new WatchList();
            }
            logger.info("addStockToWatchList::"+wt.toString());
            String hs = wt.getStocks();
            if (hs ==null || hs.isEmpty())
            {
                hs="" + st.getStockSymbol();
            }
            else
                hs = hs + "|" + st.getStockSymbol();

            wt.setStocks(hs);
            user.setWatchList(wt);
            logger.info("addStockToWatchList::"+user.getWatchList().toString());
            flushAndClear();
        }

        logger.info("addStockToWatchList::done updating");
        return true;
		
    }
    
    public Stock checkIfStockExists(String symbol) throws NoResultException {
        logger.info("Getting stock data from database for user ");
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
    
    public Set<Stock> retrieveWatchList (User user)
    {
        logger.info("retrieveWatchList::"+user.getEmail());
        Set<Stock> stockList = new HashSet<Stock>();

        WatchList wt = user.getWatchList();
        if (wt ==null)
        {
            return new HashSet<Stock>();
        }

        String stocks = wt.getStocks();
        String[] pipe_split = stocks.split("\\|");

        for (String st : pipe_split)
        {
            Stock s =null;
            try {
                s = checkIfStockExists(st);
            } catch (Exception e) {
                s=null;
            }

            if (s!=null)
                stockList.add(s);
        }
        logger.info("retrieveWatchList::size::"+stockList.size());
        return stockList;
        
    }


}