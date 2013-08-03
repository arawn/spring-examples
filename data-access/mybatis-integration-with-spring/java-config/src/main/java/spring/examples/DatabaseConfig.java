package spring.examples;

import com.googlecode.flyway.core.Flyway;
import com.jolbox.bonecp.BoneCPDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import spring.examples.core.mybatis.Mapper;

import javax.sql.DataSource;

/**
 * Data Access Layer 설정 빈
 *
 * EnableTransactionManagement는 {@link org.springframework.transaction.annotation.Transactional}
 * 애노테이션 기반 스프링 트랜잭션 처리에 필요한 환경을 구성해준다.
 *
 * MapperScan은 spring.examples.module 패키지 이하에서 {@link spring.examples.core.mybatis.Mapper}
 * 애노테이션이 선언된 클래스를 찾아 스프링 빈으로 등록한다.
 *
 * @author arawn.kr@gmail.com
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "spring.examples.module", annotationClass = Mapper.class)
public class DatabaseConfig implements InitializingBean {

    @Autowired
    private Environment environment;

    /**
     * {@link javax.sql.DataSource}를 빈으로 등록한다.
     * {@link spring.examples.EnvironmentInitializer}에 의해 등록된 JDBC 설정정보를 사용한다.
     *
     * BoneCP는 오픈소스 JDBC Pool 라이브러리이다.
     * 같은 일을 하는 라이브러리로 Tomcat JDBC Pool(Apache DBCP), c3p0 등이 있다.
     */
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(environment.getRequiredProperty("jdbc.driverClass"));
        dataSource.setJdbcUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));

        return dataSource;
    }

    /**
     * 스프링이 트랜잭션을 관리할때 사용하는 트랜잭션매니저를 등록한다.
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * 스프링 빈 팩토리에 등록된 후 호출되는 초기화 메소드이다. ({@link InitializingBean} 인터페이스 구현)
     *
     * Flyway를 초기화하고 [src/main/resources/db/migration] 위치에 있는 SQL 파일들을 등록된 DataSource에 적용한다.
     *
     * Flyway는 Java 기반의 오픈소스 데이터베이스 마이그레이션 도구이다.
     * 자세한 사용법은 <a href="http://flywaydb.org/">Flyway Page</a>에 있다.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource());
        flyway.init();
        flyway.migrate();
    }

}
