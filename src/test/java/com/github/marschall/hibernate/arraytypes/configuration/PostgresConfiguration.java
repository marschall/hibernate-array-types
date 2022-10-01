package com.github.marschall.hibernate.arraytypes.configuration;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.postgresql.Driver;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@Configuration
public class PostgresConfiguration {

  @Bean
  public DataSource dataSource() {
    if (!Driver.isRegistered()) {
      try {
        Driver.register();
      } catch (SQLException e) {
        throw new BeanCreationException("could not register driver", e);
      }
    }
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.setSuppressClose(true);
    String userName = "jdbc";
    dataSource.setUrl("jdbc:postgresql:" + userName);
    dataSource.setUsername(userName);

    String password = "Cent-Quick-Space-Bath-8";
    dataSource.setPassword(password);
    return DataSourceUtils.wrapWithLogging(dataSource, "postgres-test");
  }

}
