package com.cognizant.influentia.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
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
	basePackages = "com.cognizant.influentia.contentms",
	entityManagerFactoryRef = "contentMSEntityManagerFactory",
	transactionManagerRef = "contentMSTransactionManager"
	)
public class ContentMSDataSourceConfiguration {
	
	@Autowired
	private Environment env;

	@Primary
	@Bean
	@ConfigurationProperties("spring.datasource.contentms")
	public DataSourceProperties contentMSDataSourceProperties() {
		return new DataSourceProperties();
	}
	
	@Primary
	@Bean
	public DataSource contentMSDataSource() {
		return contentMSDataSourceProperties().initializeDataSourceBuilder().build();
	}
	
	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean contentMSEntityManagerFactory() {
		
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(contentMSDataSource());
		em.setPackagesToScan(new String[] {"com.cognizant.influentia.contentms.entity"});
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
	
	@Primary
	@Bean
	public PlatformTransactionManager contentMSTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(contentMSEntityManagerFactory().getObject());
		return transactionManager;
	}
}