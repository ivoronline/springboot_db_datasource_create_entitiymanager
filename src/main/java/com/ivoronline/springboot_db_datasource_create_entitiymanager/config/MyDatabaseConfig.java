package com.ivoronline.springboot_db_datasource_create_entitiymanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
  basePackages            = "com.ivoronline.springboot_db_datasource_create_entitiymanager.repository",
  entityManagerFactoryRef = "myEntityManagerFactoryBean"
)
public class MyDatabaseConfig {

	@Autowired private Environment env;

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
  LocalContainerEntityManagerFactoryBean myEntityManagerFactoryBean (
    EntityManagerFactoryBuilder entityManagerFactoryBuilder,
    DataSource                  dataSource
  ) {
  
    //Hibernate Properties from application.properties are ignored => We need to set them manually
    HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
                              hibernateJpaVendorAdapter.setGenerateDdl(true);
                              hibernateJpaVendorAdapter.setShowSql(true);
  
    LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
      entityManagerFactory.setDataSource(dataSource);
      entityManagerFactory.setPackagesToScan("com.ivoronline.springboot_db_datasource_create_entitiymanager.entity");
      entityManagerFactory.setPersistenceUnitName("myunit");
      entityManagerFactory.setJpaVendorAdapter(hibernateJpaVendorAdapter); //Properties don't work without it
      entityManagerFactory.setJpaProperties(additionalProperties());
      
    return entityManagerFactory;
    
  }
  
   //=========================================================================================================
  // ADDITIONAL PROPERTIES
  //=========================================================================================================
  // Reading Hibernate Properties from application.properties
  private Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.setProperty("hibernate.show_sql"    , env.getProperty("spring.jpa.show-sql"          ));
		return properties;
	}
	
}
