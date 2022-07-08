package phoenixit.education.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
public class MySQLDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasources.mysql")
    public DataSourceProperties mysqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasources.mysql.hikari")
    public HikariDataSource mysqlDataSource(@Qualifier("mysqlDataSourceProperties") DataSourceProperties mysqlDataSourceProperties) {

        return mysqlDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasources.mysql.jpa")
    public JpaProperties mysqlJpaProperties() {
        return new JpaProperties();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mysqlEntityManager(@Qualifier("mysqlJpaProperties") JpaProperties mysqlJpaProperties,
                                                                     @Qualifier("mysqlDataSource") DataSource mysqlDataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mysqlDataSource);
        em.setPackagesToScan("phoenixit.education.components");
        em.setPersistenceUnitName("mysql");
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaPropertyMap(mysqlJpaProperties.getProperties());
        return em;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager mysqlTransactionManager(@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean mysqlEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(mysqlEntityManager.getObject());
        return transactionManager;
    }


}
