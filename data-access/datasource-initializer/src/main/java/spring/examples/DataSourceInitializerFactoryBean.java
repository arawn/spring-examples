package spring.examples;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author arawn.kr@gmail.com
 */
public class DataSourceInitializerFactoryBean implements FactoryBean<DataSourceInitializer>
                                                       , ResourceLoaderAware
                                                       , InitializingBean {

    private DataSourceInitializer dataSourceInitializer;

    private DataSource dataSource;

    private Set<String> populatorScripts = new LinkedHashSet<String>();
    private Set<String> cleanerScripts = new LinkedHashSet<String>();

    private boolean enabled = true;
    private IgnoreFailures ignoreFailures;

    private ResourceLoader resourceLoader;

    @Override
    public Class<?> getObjectType() {
        return DataSourceInitializer.class;
    }

    @Override
    public DataSourceInitializer getObject() throws Exception {
        return dataSourceInitializer;
    }

    private DatabasePopulator createDatabasePopulator(Set<String> scripts, IgnoreFailures ignoreFailures) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        for(String script : populatorScripts) {
            databasePopulator.addScript(resourceLoader.getResource(script));
        }

        if(ignoreFailures == IgnoreFailures.DROPS) {
            databasePopulator.setIgnoreFailedDrops(true);
        } else if(ignoreFailures == IgnoreFailures.ALL) {
            databasePopulator.setContinueOnError(true);
        }

        return databasePopulator;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public DataSourceInitializerFactoryBean setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;

        return this;
    }

    public DataSourceInitializerFactoryBean addScript(String script) {
        this.populatorScripts.add(script);

        return this;
    }

    public DataSourceInitializerFactoryBean addCleanerScript(String cleanerScript) {
        this.cleanerScripts.add(cleanerScript);

        return this;
    }

    public DataSourceInitializerFactoryBean setEnabled(boolean enabled) {
        this.enabled = enabled;

        return this;
    }

    public DataSourceInitializerFactoryBean setIgnoreFailures(IgnoreFailures ignoreFailures) {
        this.ignoreFailures = ignoreFailures;

        return this;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(resourceLoader);
        Assert.notNull(dataSource);

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setEnabled(enabled);

        if(!populatorScripts.isEmpty()) {
            initializer.setDatabasePopulator(createDatabasePopulator(populatorScripts, ignoreFailures));
        }

        if(!cleanerScripts.isEmpty()) {
            initializer.setDatabaseCleaner(createDatabasePopulator(cleanerScripts, ignoreFailures));
        }

        this.dataSourceInitializer = initializer;
    }


    static enum IgnoreFailures {

        NONE, DROPS, ALL;

    }

}
