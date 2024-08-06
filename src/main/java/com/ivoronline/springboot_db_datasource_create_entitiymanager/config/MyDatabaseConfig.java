package com.ivoronline.springboot_db_datasource_create_entitiymanager.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
  basePackages            = "com.ivoronline.springboot_db_datasource_create_entitiymanager.repository",
  entityManagerFactoryRef = "myEntityManagerFactoryBean"
)
public class MyDatabaseConfig {

  //=========================================================================================================
  // DATA SOURCE PROPERTIES
  //=========================================================================================================
  @Bean
  @ConfigurationProperties("my.spring.datasource")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }
  
  //=========================================================================================================
  // DATA SOURCE
  //=========================================================================================================
  @Bean
  public DataSource dataSource() {
    return dataSourceProperties().initializeDataSourceBuilder().build();
  }
  
  //=========================================================================================================
  // ENTITY MANAGER FACTORY BEAN
  //=========================================================================================================
  @Bean
  public LocalContainerEntityManagerFactoryBean customEntityManagerFactory(
      DataSource          customDataSource,
      HibernateProperties hibernateProperties
    ) {
      
      LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
      em.setDataSource(customDataSource);
      em.setPackagesToScan("com.stackoverflow.entites"); // Set the package to scan for entities

      HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
      em.setJpaVendorAdapter(vendorAdapter);

      Map<String, Object> properties = hibernateProperties.determineHibernateProperties(
              HibernateProperties.getProperties(), new HibernateSettings());

      properties.put("hibernate.hbm2ddl.auto", "create"); // Customize the ddl-auto setting here

      em.setJpaPropertyMap(properties);

      return em;
  }

  
}


