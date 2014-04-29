DataSource 초기화
========================================

`org.springframework.jdbc.datasource.init` 패키지는 `javax.sql.DataSource` 초기화를 지원한다.


### XML 네임스페이스(namespace)를 사용해 데이터베이스 초기화하기

spring-jdbc 네임스페이스의 initialize-database 태그로 데이터베이스를 초기화 할 수 있다.

```xml
<jdbc:initialize-database data-source="dataSource">
  <jdbc:script location="classpath:db/migration/create_member_table.sql"/>
  <jdbc:script location="classpath:db/migration/insert_member_table.sql"/>
</jdbc:initialize-database>
```

위 예제는 지정한 DataSource에 등록된 두 개의 스크립트를 실행한다. 첫 스크립트는 스키마를 생성하고 두번째 스크립트는 데이터를 등록한다.
환경변수에 따라 실행되지 못하게 막거나, 오류를 무시하고 그대로 진행할 수 있는 기능도 함께 제공된다.

```xml
<jdbc:initialize-database data-source="dataSource" enabled="true|false" ignore-failures="NONE|DROPS|ALL">
  <jdbc:script location="..."/>
</jdbc:initialize-database>
```

enabled는 활성화 플래그 값이고, ignore-failures는 특정 SQL 오류를 무시 할 수 있는 옵션이다.
자세한건 [Spring Framework Reference Documentation](http://docs.spring.io/spring/docs/4.0.2.RELEASE/spring-framework-reference/htmlsingle)에 Initializing a DataSource 섹션에서 확인 할 수 있다.


### Java Configuration에서 데이터베이스 초기화하기

`org.springframework.jdbc.datasource.init` 패키지에 있는 `DataSourceInitializer`와 `DatabasePopulator`로 데이터베이스를 초기화 할 수 있다.

spring-jdbc 네임스페이스의 initialize-database 태그가 하는 일도 위 2개의 클래스를 스프링 빈 컨테이너에 등록하는 일이다.

```java
@Bean
public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
    ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
    databasePopulator.addScript(new ClassPathResource("db/migration/create_member_table.sql"));
    databasePopulator.addScript(new ClassPathResource("db/migration/insert_member_table.sql"));
    databasePopulator.setIgnoreFailedDrops(true);

    DataSourceInitializer initializer = new DataSourceInitializer();
    initializer.setDataSource(dataSource);
    initializer.setDatabasePopulator(databasePopulator);

    return initializer;
}
```

위 예제는 XML 네임스페이스 예제와 동일한 일을 수행한다. `DataSourceInitializer`를 사용하면 XML 비해 더 정밀한 전략을 구사할 수 있다.

```java
@Bean
public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
    ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
    databasePopulator.addScript(new ClassPathResource("db/migration/create_member_table.sql"));
    databasePopulator.addScript(new ClassPathResource("db/migration/insert_member_table.sql"));

    ResourceDatabasePopulator databaseCleaner = new ResourceDatabasePopulator();
    databaseCleaner.addScript(new ClassPathResource("db/migration/drop_member_table.sql"));
    databaseCleaner.setIgnoreFailedDrops(true);

    DataSourceInitializer initializer = new DataSourceInitializer();
    initializer.setDataSource(dataSource);
    initializer.setDatabasePopulator(databasePopulator);
    initializer.setDatabaseCleaner(databaseCleaner);

    return initializer;
}
```

`DataSourceInitializer.setDatabaseCleaner(DatabasePopulator)`를 넣어두면 빈 컨테이너가 종료될때 데이터베이스를 정리한다.

DataSourceInitializer를 생성하는 방법이 복잡하다면 FactoryBean을 사용해 생성 로직을 숨기고 재사용 할 수도 있다.

```java
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
```

`DataSourceInitializerFactoryBean`의 소스 코드는 [여기](https://github.com/arawn/spring-examples/blob/master/data-access/datasource-initializer/src/main/java/spring/examples/DataSourceInitializerFactoryBean.java)서 볼 수 있다.
