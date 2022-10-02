package com.github.marschall.hibernate.arraytypes;

import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.github.marschall.hibernate.arraytypes.configuration.HsqldbConfiguration;
import com.github.marschall.hibernate.arraytypes.configuration.SpringHibernateConfiguration;

@Disabled
@TestPropertySource(properties = "persistence-unit-name=hsqldb-batched")
@SpringJUnitConfig({HsqldbConfiguration.class, SpringHibernateConfiguration.class})
class HsqldbArrayTests extends AbstractArrayTests {


}