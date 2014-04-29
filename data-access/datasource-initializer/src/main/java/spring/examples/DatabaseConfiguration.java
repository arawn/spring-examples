package spring.examples;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static spring.examples.DataSourceInitializerFactoryBean.IgnoreFailures;

/**
 * @author arawn.kr@gmail.com
 */
@Configuration
@PropertySource("classpath:META-INF/dataSourceProperties.xml")
@EnableTransactionManagement
public class DatabaseConfiguration {



    @Bean
    public DataSourceProperties dataSourceProperties(Environment environment) {
        return new DataSourceProperties(environment);
    }

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource, ResourceLoader resourceLoader) throws Exception {
        DataSourceInitializerFactoryBean factoryBean = new DataSourceInitializerFactoryBean();

        factoryBean.setDataSource(dataSource)
                   .setIgnoreFailures(IgnoreFailures.DROPS)
                   .setResourceLoader(resourceLoader);

        factoryBean.addScript("classpath:db/migration/create_member_table.sql")
                   .addScript("classpath:db/migration/insert_member_table.sql")
                   .addCleanerScript("classpath:db/migration/drop_member_table.sql");

        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }


    class DataSourceProperties {

        private Environment environment;

        public DataSourceProperties(Environment environment) {
            this.environment = environment;
        }

        public String getDriverClassName() {
            return environment.getRequiredProperty("jdbc.driverClass");
        }

        public String getUrl() {
            return environment.getRequiredProperty("jdbc.url");
        }

        public String getUsername() {
            return environment.getRequiredProperty("jdbc.username");
        }

        public String getPassword() {
            return environment.getRequiredProperty("jdbc.password");
        }

    }

}
