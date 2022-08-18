package com.senla.config;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@EnableTransactionManagement
@ComponentScan(basePackages = "com.senla")
@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /*@Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("configuration/config.properties"));

        Properties prop = new Properties();
        try {
            prop.store(new FileOutputStream("config.properties"), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        prop.setProperty("log.folder", System.getProperty("user.dir")); //path to project folder
        return configurer;
    }*/

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setDataSource(dataSource());
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public DataSource dataSource() {
        Properties prop = new Properties();
        try {
            prop.load(AppConfig.class.getClassLoader().getResourceAsStream("configuration/config.properties"));
        } catch (IOException e) {
            log.error("Не удалось открыть property файл", e);
            throw new RuntimeException(e);
        }

        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(prop.getProperty("spring.datasource.driver-class-name"));
        driver.setUrl(prop.getProperty("spring.datasource.url"));
        driver.setUsername(prop.getProperty("spring.datasource.username"));
        driver.setPassword(prop.getProperty("spring.datasource.password"));
        return driver;
    }
}
