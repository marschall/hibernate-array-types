package com.github.marschall.hibernate.arraytypes;

import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.github.marschall.hibernate.arraytypes.configuration.H2Configuration;
import com.github.marschall.hibernate.arraytypes.configuration.SpringHibernateConfiguration;

@TestPropertySource(properties = "persistence-unit-name=h2-batched")
@SpringJUnitConfig({H2Configuration.class, SpringHibernateConfiguration.class})
class H2ArrayTests extends AbstractArrayTests {


}
