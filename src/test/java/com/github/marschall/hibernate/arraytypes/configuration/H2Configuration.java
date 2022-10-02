package com.github.marschall.hibernate.arraytypes.configuration;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

@Configuration
public class H2Configuration {

  @Bean
  public DataSource dataSource() {
    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
            .generateUniqueName(true)
            .setType(H2)
            .addScript("sql/h2/01_tables.sql");
    return DataSourceUtils.wrapWithLogging(builder.build(), "h2-test");
  }

}
