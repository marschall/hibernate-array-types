package com.github.marschall.hibernate.arraytypes.configuration;

import javax.sql.DataSource;

import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

final class DataSourceUtils {

  private DataSourceUtils() {
    throw new AssertionError("not instantiable");
  }

  static DataSource wrapWithLogging(DataSource dataSource, String name) {

    SLF4JQueryLoggingListener loggingListener = new SLF4JQueryLoggingListener();
    loggingListener.setWriteDataSourceName(false);
    loggingListener.setWriteConnectionId(false);
    loggingListener.setQueryLogEntryCreator(new DefaultQueryLogEntryCreator());
    loggingListener.setLogLevel(SLF4JLogLevel.INFO);
    return ProxyDataSourceBuilder
            .create(dataSource)
            .name(name)
            .listener(loggingListener)
            .build();
  }

}
