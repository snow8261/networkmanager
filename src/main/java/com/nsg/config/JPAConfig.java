package com.nsg.config;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.nsg.persistence.repository.ManagedelementsRepository;

@Configuration
@EnableJpaRepositories(basePackages = "com.nsg.persistence.repository", includeFilters = @ComponentScan.Filter(value = { ManagedelementsRepository.class }, type = FilterType.ASSIGNABLE_TYPE))
@EnableTransactionManagement
public class JPAConfig {
	//@Bean
	public DataSource dataSource() throws SQLException {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		DataSource ds = builder.setType(EmbeddedDatabaseType.H2).build();
		String[] args = "-web,-webAllowOthers,-webPort,8082".split(",");
		Server webServer = Server.createWebServer(args).start();
		return ds;
	}

	//@Bean 
	public String h2Server()throws SQLException{
		String[] args = "-web,-webAllowOthers,-webPort,8082".split(",");
	     Server.createWebServer(args).start();
	     return "start";
	}
	
//	@Bean
	public DataSource dataSource2() throws SQLException {
		SimpleDriverDataSource simple = new SimpleDriverDataSource();
		// simple.setDriver(org.h2.Driver.load());
		simple.setDriverClass(org.h2.Driver.class);
		simple.setUrl(String.format("jdbc:h2:mem:%s;MVCC=TRUE;MODE=Oracle;DB_CLOSE_DELAY=-1",
				"testdb"));
		simple.setUsername("sa");
		simple.setPassword("");
		return simple;
	}

	@Bean 
	public DataSource dataSourceOracle() throws SQLException{
		OracleDataSource ds=new OracleDataSource();
		ds.setURL("jdbc:oracle:thin:@//134.74.25.2:1522/sdh");
		ds.setUser("bill");
		ds.setPassword("bill");
		
		return ds;
	}
	
	@Bean
	public EntityManagerFactory entityManagerFactory() throws SQLException {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.nsg.persistence.domain");
		factory.setDataSource(dataSourceOracle());
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean
	public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
		return entityManagerFactory.createEntityManager();
	}

	@Bean
	public PlatformTransactionManager transactionManager() throws SQLException {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}
}
