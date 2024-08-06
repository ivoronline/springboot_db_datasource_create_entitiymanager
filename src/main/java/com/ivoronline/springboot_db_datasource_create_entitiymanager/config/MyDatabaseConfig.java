package com.ivoronline.springboot_db_datasource_create_entitiymanager.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import javax.sql.DataSource;

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
  // ENTITY MANAGER FACTORY
  //=========================================================================================================
  @Bean
  LocalContainerEntityManagerFactoryBean myEntityManagerFactoryBean() {
    
    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource      (dataSource());
    emf.setPackagesToScan  ("com.ivoronline.springboot_db_datasource_create_entitiymanager.entity");
    emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    
    return emf ;
    
  }
  
}
