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
	basePackages = "com.cognizant.influentia.subscriptionms",
	entityManagerFactoryRef = "subscriptionMSEntityManagerFactory",
	transactionManagerRef = "subscriptionMSTransactionManager"
	)
public class SubscriptionMSDataSourceConfiguration {
	
	@Autowired
	private Environment env;

	@Bean
	@ConfigurationProperties("spring.datasource.subscriptionms")
	public DataSourceProperties subscriptionMSDataSourceProperties() {
		return new DataSourceProperties();
	}
	
	@Bean
	public DataSource subscriptionMSDataSource() {
		return subscriptionMSDataSourceProperties().initializeDataSourceBuilder().build();
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean subscriptionMSEntityManagerFactory() {
		
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(subscriptionMSDataSource());
		em.setPackagesToScan(new String[] {"com.cognizant.influentia.subscriptionms.entity"});
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setShowSql(true);
		vendorAdapter.setGenerateDdl(true);
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
		properties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
		em.setJpaPropertyMap(properties);
		return em;
	}
	
	@Bean
	public PlatformTransactionManager subscriptionMSTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(subscriptionMSEntityManagerFactory().getObject());
		return transactionManager;
	}
}