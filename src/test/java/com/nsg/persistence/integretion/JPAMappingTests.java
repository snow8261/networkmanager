package com.nsg.persistence.integretion;

import static com.nsg.persistence.domain.helper.JPAAssertions.assertTableExists;
import static com.nsg.persistence.domain.helper.JPAAssertions.assertTableHasColumn;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.nsg.config.JPAConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JPAConfig.class })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class JPAMappingTests {
	@Autowired
	EntityManager manager;

	@Test
	public void thatItemCustomMappingWorks() throws Exception {
		assertTableExists(manager, "Managedelement");
		assertTableHasColumn(manager, "Managedelement", "id");
		assertTableHasColumn(manager, "Managedelement", "name");
	}  
}
