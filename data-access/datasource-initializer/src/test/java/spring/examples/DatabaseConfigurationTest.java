package spring.examples;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DatabaseConfiguration.class)
public class DatabaseConfigurationTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    @Test
    public void testIfExistsMemberTable() {
        Integer count = jdbcTemplate.queryForObject("select count(id) from member", Integer.class);

        assertThat(count, is(2));
    }

}