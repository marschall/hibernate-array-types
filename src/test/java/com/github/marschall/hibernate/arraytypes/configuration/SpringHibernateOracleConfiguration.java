package com.github.marschall.hibernate.arraytypes.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import oracle.jdbc.OracleConnection;

@Configuration
public class SpringHibernateOracleConfiguration {

  private static final String PERSISTENCE_UNIT_NAME = "oracle-batched";

  @Bean
  public DataSource dataSource() {
    oracle.jdbc.OracleDriver.isDebug();
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.setSuppressClose(true);
    dataSource.setUrl("jdbc:oracle:thin:@localhost:1521/ORCLPDB1");
    dataSource.setUsername("jdbc");
    dataSource.setPassword("Cent-Quick-Space-Bath-8");
    Properties connectionProperties = new Properties();
//    connectionProperties.setProperty("oracle.net.disableOob", "true");
    connectionProperties.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_NET_DISABLE_OUT_OF_BAND_BREAK, "true");
    dataSource.setConnectionProperties(connectionProperties);
    return dataSource;
  }

//  @Bean
//  public DatabasePopulator databasePopulator() {
//    ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
//            new ClassPathResource("oracle-schema.sql"));
//    populator.setSeparator("!!");
//    return populator;
//  }

  @Bean
  public PlatformTransactionManager txManager(EntityManagerFactory emf) {
    JpaTransactionManager transactionManager = new JpaTransactionManager(emf);
    transactionManager.setDataSource(this.dataSource());
    transactionManager.setJpaDialect(jpaDialect());
    transactionManager.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
    return transactionManager;
  }
  
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManager() {
    LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
    bean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
    bean.setJpaDialect(jpaDialect());
    bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    bean.setDataSource(dataSource());
    return bean;
  }

  @Bean
  public JpaDialect jpaDialect() {
    return new HibernateJpaDialect();
  }

}
