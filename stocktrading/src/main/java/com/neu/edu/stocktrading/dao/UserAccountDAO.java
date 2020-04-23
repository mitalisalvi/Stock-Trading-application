package com.neu.edu.stocktrading.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.neu.edu.stocktrading.model.User;
import com.neu.edu.stocktrading.model.UserBankDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserAccountDAO {
	
	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger logger = LoggerFactory.getLogger(UserAccountDAO.class);

	public User getProfileAttributes(String email) {
		logger.info("Getting user from email : " + email);
		TypedQuery<User> query = this.entityManager.createQuery("SELECT u from User u where u.email = ?1", User.class);
		query.setParameter(1, email);
		return query.getSingleResult();
	}

	@Transactional
	public User updateProfileAttributes(String email  , String phoneNumber) {
		logger.info("Updating user using new user object::"+phoneNumber);
		User getUserFromEmail = getProfileAttributes(email);
		logger.info("updated id::"+getUserFromEmail.getId());
		getUserFromEmail = this.entityManager.find(User.class, getUserFromEmail.getId());

		getUserFromEmail.setPhoneNumber(phoneNumber);

		flushAndClear();
		return getUserFromEmail;

	}

	@Transactional
	public User updateBankDetails(User user, UserBankDetails userBank) 
	{
		logger.info("updateBankDetails::"+userBank.getAccountNumber());
		userBank = this.saveUserBank(userBank);
		user.setUserBankDetails(userBank);
		flushAndClear();
		return user;

	}

	@Transactional
	public UserBankDetails saveUserBank(UserBankDetails userBank) 
	{
		logger.info("Saving userbank object to database");
		return this.entityManager.merge(userBank);
    }

	private void flushAndClear() {
		this.entityManager.flush();
		this.entityManager.clear();
	}

}