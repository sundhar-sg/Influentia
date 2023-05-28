package com.cognizant.influentia.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	basePackages = "com.cognizant.influentia.accountms",
	entityManagerFactoryRef = "accountMSEntityManagerFactory",
	transactionManagerRef = "accountMSTransactionManager"
	)
public class AccountMSDataSourceConfiguration {
	
	@Autowired
	private Environment env;

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.accountms")
	public DataSourceProperties accountMSDataSourceProperties() {
		return new DataSourceProperties();
	}
	
	@Bean
	public DataSource accountMSDataSource() {
		return accountMSDataSourceProperties().initializeDataSourceBuilder().build();
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean accountMSEntityManagerFactory() {
		
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(accountMSDataSource());
		em.setPackagesToScan(new String[] {"com.cognizant.influentia.accountms.entity"});
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
		properties.put("hibernate.show-sql", true);
		properties.put("hibernate.format_sql", true);
		em.setJpaPropertyMap(properties);
		return em;
	}
	
	@Bean
	public PlatformTransactionManager accountMSTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(accountMSEntityManagerFactory().getObject());
		return transactionManager;
	}
}