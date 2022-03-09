package com.youcode.config;

import java.util.Properties;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@Configuration
@PropertySource("classpath:hibernate.cfg.xml")
@EnableTransactionManagement
public class AppContext {
	@Autowired
    private Environment environment;

    @Bean("sessionFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan(new String[] {
            "com.youcode.entity"
        });
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        return sessionFactoryBean;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("com.mysql.jdbc.Driver"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc:mysql://localhost:3306/mydb"));
        dataSource.setUsername(environment.getRequiredProperty("root"));
        dataSource.setPassword(environment.getRequiredProperty("0000"));
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("org.hibernate.dialect.MySQL5Dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("true"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("true"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("update"));
        return properties;
    }

    @Bean
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

}
