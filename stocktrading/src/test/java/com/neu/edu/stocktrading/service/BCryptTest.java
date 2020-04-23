package com.neu.edu.stocktrading.service;

import org.junit.Assert;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptTest {
	
	@Test
	public void generateEncryptedPasswordTest() 
	{
		String actualEncryptedPass = BCrypt.hashpw("Pass@123", BCrypt.gensalt());
		Assert.assertTrue(BCrypt.checkpw("Pass@123", actualEncryptedPass));
	}
}