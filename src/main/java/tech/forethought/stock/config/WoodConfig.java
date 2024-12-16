package tech.forethought.stock.config;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

import javax.sql.DataSource;

@Configuration
public class WoodConfig {
    @Bean(name = "db", typed = true)
    public DataSource db(@Inject("${wood.db}") HikariDataSource ds) {
        return ds;
    }
}
