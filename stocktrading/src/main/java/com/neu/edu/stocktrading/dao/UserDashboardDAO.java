package com.neu.edu.stocktrading.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.neu.edu.stocktrading.model.Stock;
import com.neu.edu.stocktrading.model.Trade;
import com.neu.edu.stocktrading.model.Transaction;
import com.neu.edu.stocktrading.model.User;
import com.neu.edu.stocktrading.model.WatchList;
import com.neu.edu.stocktrading.util.FileReaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserDashboardDAO {
	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger logger = LoggerFactory.getLogger(UserDashboardDAO.class);

	public User getProfileAttributes(String email) {
		logger.info("Getting user from email : " + email);
		TypedQuery<User> query = this.entityManager.createQuery("SELECT u from User u where u.email = ?1", User.class);
		query.setParameter(1, email);
		query.setMaxResults(1);
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

	@Transactional
	public Transaction createTransaction(User user) {
		logger.info("createTransaction::" + user.getId());
		Transaction transaction = new Transaction();
		transaction.setUser(user);
		transaction.setTimestampdate(new Date());
		this.entityManager.merge(transaction);
		flushAndClear();
		logger.info("DONE createTransaction::" + transaction.toString());
		return transaction;
	}

	private void flushAndClear() {
		this.entityManager.flush();
		this.entityManager.clear();
	}

	public Stock checkIfStockExists(String symbol) throws NoResultException {
		logger.info("Getting stock data from database for user ");
		TypedQuery<Stock> query = this.entityManager.createQuery("SELECT c from Stock c where c.stockSymbol = ?1",
				Stock.class);
		query.setParameter(1, symbol);
		query.setMaxResults(1);
		return query.getSingleResult();
	}

	@Transactional
	public Trade createSellTrade(User user, Stock s, String quantity, Transaction trans) {
		Trade trade = new Trade();
		trade.setBuySell("Sell");
		trade.setIndividualPrice(s.getBuyingPrice());
		trade.setQuantity(Integer.parseInt(quantity));

		Transaction t = null;
		try {
			t = this.getLatestTransaction(user.getEmail());
		} catch (Exception e) {
			t = null;
		}

		if (t != null) {
			trade.setTransId(Long.toString(t.getId()));
			trade.setTransaction(t);
		}

		trade.setStock(s);
		this.entityManager.merge(trade);
		flushAndClear();

		return trade;
	}

	public Transaction getLatestTransaction(String email) {
		logger.info("getLatestTransaction::");
		User user = this.getProfileAttributes(email);
		logger.info("getLatestTransaction::" + user.getId());
		TypedQuery<Transaction> query = this.entityManager
				.createQuery("SELECT a from Transaction a where a.user = ?1 order by a.id desc", Transaction.class);
		query.setParameter(1, user);
		query.setMaxResults(1);
		return query.getSingleResult();
	}

	@Transactional
	public void updateSellMap(User user, Set<Trade> trades) {
		logger.info("updateSellMap::INSIDE");
		User u = null;
		try {
			u = getWatchListFromUser(user);
		} catch (Exception e) {
			u = null;
		}

		if (u != null) {
			WatchList wt = u.getWatchList();
			Map<Integer, Integer> stockToAmount = wt.getStockToAmount();
			logger.info("BEFORE updateSellMap:" + stockToAmount);
			for (Trade t : trades) {
				long id = t.getStock().getId();
				logger.info("updateSellMap:" + t.getStock().getStockSymbol());
				logger.info("updateSellMap:" + t.getStock().getId());
				Stock s = this.getStockFromId(id);
				Long l = Long.valueOf(id);
				Integer qw = l.intValue();
				if (stockToAmount.containsKey(qw)) {
					logger.info("updateSellMap:::UPDATING");
					stockToAmount.put(qw, (stockToAmount.get(qw) - t.getQuantity()));
				}
			}
			logger.info("" + stockToAmount);
			wt.setStockToAmount(stockToAmount);
			user.setWatchList(wt);
			flushAndClear();

			logger.info("updateSellMap::DONE");
		}
	}

	public User getWatchListFromUser(User user) throws NoResultException {
		logger.info("getWatchListFromUser::" + user.getEmail());
		TypedQuery<User> query = this.entityManager.createQuery("SELECT c from User c where c.email = ?1", User.class);
		query.setParameter(1, user.getEmail());
		return query.getSingleResult();
	}

	@Transactional
	public Transaction updateSellTransactions(Set<Trade> st, User user) {
		logger.info("updateSellTransactions::INSIDE");

		Transaction result = null;
		try {
			result = this.getLatestTransaction(user.getEmail());
		} catch (Exception e) {
			result = null;
		}

		double totalPriceBuy = 0.0;
		for (Trade t : st) {
			totalPriceBuy += (t.getIndividualPrice() * t.getQuantity());
		}

		Double serviceCharge = Double.parseDouble(FileReaderUtil.readServiceChargeValue());
		totalPriceBuy = totalPriceBuy + (serviceCharge / 100 * totalPriceBuy);
		logger.info("updateSellTransactions::totalPriceBuy::" + totalPriceBuy);

		if (result != null) {
			logger.info("NOT NULL::");
			result.setTotalPrice(totalPriceBuy);
		}

		flushAndClear();
		logger.info("updateSellTransactions::DONE");

		return result;
	}

	public List<Trade> getTradesUsingTransaction(long transactionId, String buySell) {
		logger.info("getTradesUsingTransaction::" + transactionId + "  " + buySell);

		CriteriaBuilder qb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery cq = qb.createQuery();
		Root<Trade> customer = cq.from(Trade.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (transactionId != 0.0) {
			predicates.add(qb.equal(customer.get("transId"), transactionId));
		}
		if (buySell != null) {
			predicates.add(qb.equal(customer.get("buySell"), buySell));
		}
		// query itself
		cq.select(customer).where(predicates.toArray(new Predicate[] {}));

		List<Trade> result = this.entityManager.createQuery(cq).getResultList();

		logger.info("getTradesUsingTransaction::" + result.size());

		return result;

	}

}