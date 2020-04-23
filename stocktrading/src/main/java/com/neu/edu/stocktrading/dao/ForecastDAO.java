package com.neu.edu.stocktrading.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public class ForecastDAO {

	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger logger = LoggerFactory.getLogger(ForecastDAO.class);

	public User getProfileAttributes(String email) 
	{
		logger.info("Getting user from email : " + email);
		TypedQuery<User> query = this.entityManager.createQuery("SELECT u from User u where u.email = ?1", User.class);
		query.setParameter(1, email);
		return query.getSingleResult();
	}

	public Map<String, List<? extends Object>> retrieveSellList(User user) {

		List<Stock> sellingList = new ArrayList<Stock>();
		List<Integer> sellingListAmount = new ArrayList<Integer>();

		User u = getProfileAttributes(user.getEmail());
		WatchList wt = u.getWatchList();

		if (wt != null) {
			logger.info("watchlist id:::" + wt.getId());

			Map<Integer, Integer> map = wt.getStockToAmount();
			logger.info("map::" + map);
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				if ((int) pair.getValue() > 0) {
					Integer stockKey = (Integer) pair.getKey();
					Stock s = this.getStockFromId(Integer.toUnsignedLong((int) pair.getKey()));
					logger.info("unsold shares::" + s.getStockName());
					sellingList.add(s);
					sellingListAmount.add((int) pair.getValue());
				}
			}

		}

		Map<String, List<? extends Object>> userStockList = new HashMap<String, List<? extends Object>>();
		userStockList.put("Sell", sellingList);
		userStockList.put("Amount", sellingListAmount);

		return userStockList;

	}

	public Stock getStockFromId(long id) throws NoResultException {
		logger.info("Getting stock data from database for ID ");
		TypedQuery<Stock> query = this.entityManager.createQuery("SELECT c from Stock c where c.id = ?1", Stock.class);
		query.setParameter(1, id);
		query.setMaxResults(1);
		return query.getSingleResult();
	}

	public Stock checkIfStockExists(String symbol) throws NoResultException {
		logger.info("Getting user data from database for user ");
		TypedQuery<Stock> query = this.entityManager.createQuery("SELECT c from Stock c where c.stockSymbol = ?1",
				Stock.class);
		query.setParameter(1, symbol);
		query.setMaxResults(1);
		return query.getSingleResult();
	}

	@Transactional
	public boolean addStockToWatchList(User user, String stockSymbol) {
		logger.info("addStockToWatchList::" + stockSymbol);
		logger.info("addStockToWatchList::" + user.getFirstName());
		Stock st = null;
		try {
			st = checkIfStockExists(stockSymbol);
		} catch (Exception e) {
			st = null;
		}

		if (st != null) {
			WatchList wt = user.getWatchList();

			if (wt == null) {
				wt = new WatchList();
			}
			logger.info("addStockToWatchList::" + wt.toString());
			String hs = wt.getStocks();
			if (hs == null || hs.isEmpty()) {
				hs = "" + st.getStockSymbol();
			} else
				hs = hs + "|" + st.getStockSymbol();

			wt.setStocks(hs);
			user.setWatchList(wt);
			logger.info("addStockToWatchList::" + user.getWatchList().toString());
			flushAndClear();
		}

		logger.info("addStockToWatchList::done updating");
		return true;

	}

	private void flushAndClear() {
		this.entityManager.flush();
		this.entityManager.clear();
	}

}