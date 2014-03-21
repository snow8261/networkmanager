package com.nsg.persistence.integretion;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.nsg.config.JPAConfig;
import com.nsg.persistence.domain.Managedelement;
import com.nsg.persistence.repository.ManagedelementsRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JPAConfig.class})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ManagedelementRepositoryTests {
	  @Autowired
	  ManagedelementsRepository  merepository;
	  @Test
	  public void meInRepositoryTest(){
		  Random rand = new Random();
		  Long id=rand.nextLong()+1;
		  Managedelement me=new Managedelement();
		  me.setId(id);
		  me.setName("test");
		  me.setCreateTime(new Date());
		  merepository.save(me);
		  Managedelement getMe=merepository.findById(id);
		  assertNotNull(getMe);
		  assertEquals(id,getMe.getId());
		  
	 }
}
