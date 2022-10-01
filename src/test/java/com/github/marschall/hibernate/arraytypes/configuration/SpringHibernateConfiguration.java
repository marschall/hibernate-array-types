package com.github.marschall.hibernate.arraytypes.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SpringHibernateConfiguration {

  @Autowired
  private DataSource dataSource;

  @Value("${persistence-unit-name}")
  private String persistenceUnitName;

  @Bean
  public PlatformTransactionManager txManager(EntityManagerFactory emf) {
    JpaTransactionManager transactionManager = new JpaTransactionManager(emf);
    transactionManager.setDataSource(this.dataSource);
    transactionManager.setJpaDialect(this.jpaDialect());
    transactionManager.setPersistenceUnitName(this.persistenceUnitName);
    return transactionManager;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManager() {
    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setPersistenceUnitName(this.persistenceUnitName);
    bean.setJpaDialect(this.jpaDialect());
    bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    bean.setDataSource(this.dataSource);
    return bean;
  }

  @Bean
  public JpaDialect jpaDialect() {
    return new HibernateJpaDialect();
  }

}
