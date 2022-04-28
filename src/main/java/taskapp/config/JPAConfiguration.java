package taskapp.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.fossfloors.taskapp.backend.repository", transactionManagerRef = "jpaTransactionManager")
@EnableTransactionManagement
public class JPAConfiguration {

  private static final Logger   logger                  = LoggerFactory
      .getLogger(JPAConfiguration.class);

  private static final String[] ENTITY_PACKAGES_TO_SCAN = { "com.fossfloors.taskapp.backend.entity" };

  @Autowired
  private Environment           env;

  // @formatter:off
  @Bean
  public DataSource dataSource() {
    String username = env.getProperty("spring.datasource.username");
    String password = env.getProperty("spring.datasource.password");
    String driverClass = env.getProperty("spring.datasource.driver-class-name");
    String url = env.getProperty("spring.datasource.url");
    
    logger.debug("url: {}", url);
    logger.debug("driverClass: {}", driverClass);

    return DataSourceBuilder.create()
        .username(username)
        .password(password)
        .url(url)
        .driverClassName(driverClass)
        .build();
  }
  // @formatter:on

  @Bean
  public JpaTransactionManager jpaTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
    return transactionManager;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setJpaVendorAdapter(vendorAdaptor());
    entityManagerFactoryBean.setDataSource(dataSource());
    entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
    entityManagerFactoryBean.setPackagesToScan(ENTITY_PACKAGES_TO_SCAN);
    entityManagerFactoryBean.setJpaProperties(addProperties());
    return entityManagerFactoryBean;
  }

  private HibernateJpaVendorAdapter vendorAdaptor() {
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    return vendorAdapter;
  }

  private Properties addProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto",
        env.getProperty("spring.jpa.hibernate.ddl-auto"));
    // properties.setProperty("hibernate.dialect",
    // env.getProperty("spring.jpa.properties.hibernate.dialect"));
    properties.setProperty("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
    properties.setProperty("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
    // properties.setProperty("hibernate.format_sql",
    // env.getProperty("spring.jpa.properties.hibernate.format_sql"));
    properties.setProperty("hibernate.format_sql", "true");
    // we can add
    return properties;
  }

}
