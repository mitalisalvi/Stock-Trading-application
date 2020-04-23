package com.neu.edu.stocktrading.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.neu.edu.stocktrading.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserAuthDAO
{
    private static final Logger logger = LoggerFactory.getLogger(UserAuthDAO.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional
	public User saveUser(User user) 
	{
		logger.info("Saving user object to database");
		return this.entityManager.merge(user);
    }
	
	public int checkIfUserExists(String email) 
	{
		logger.info("Checking if user exists");
		int result = 0;
		try {
			Query query = this.entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = ?1");
			query.setParameter(1, email);
			query.setMaxResults(1);
			Long resultInLong = (Long) query.getSingleResult();
			result = Math.toIntExact(resultInLong);
		} catch (Exception e) {
			logger.error(e.toString());
			result = 0;
		}

		return result;
	}

	public User getProfileAttributes(String email) {
		logger.info("Getting user from email : " + email);
		TypedQuery<User> query = this.entityManager.createQuery("SELECT u from User u where u.email = ?1", User.class);
		query.setParameter(1, email);
		query.setMaxResults(1);
		return query.getSingleResult();
    }

}