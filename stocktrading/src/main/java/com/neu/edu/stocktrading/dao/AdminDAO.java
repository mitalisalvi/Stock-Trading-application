package com.neu.edu.stocktrading.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import javax.transaction.Transactional;

import com.neu.edu.stocktrading.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AdminDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(AdminDAO.class);

    

    public List<User> getUnverifiedUsers ()
    {
        return this.getUsers();
        
    }

    private void flushAndClear() {
		this.entityManager.flush();
		this.entityManager.clear();
    }

    public List<User> getUsers() 
	{
        List<User> result = new ArrayList<User>();
        try {
            TypedQuery<User> query = this.entityManager.createQuery("SELECT a from User a where a.verified = FALSE",User.class);
		    result= query.getResultList(); 
        } catch (Exception e) {
        }
		return result;
    }
    
    public User getProfileAttributes (String email)
    {
        logger.info("Getting user from email : " + email);
		TypedQuery<User> query = this.entityManager.createQuery("SELECT u from User u where u.email = ?1", User.class);
		query.setParameter(1, email);
		return query.getSingleResult();
    }

    @Transactional
    public void verifyUsers (String email)
    {
        User u = getProfileAttributes(email);
        u.setVerified(true);
        flushAndClear();
    }

}