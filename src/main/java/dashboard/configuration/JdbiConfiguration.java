package dashboard.configuration;

import dashboard.database.ProductInventoryDao;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class JdbiConfiguration {

    @Bean
    public Jdbi jdbi(DataSource ds) {
        Jdbi jdbi = Jdbi.create(ds);
        jdbi.installPlugin(new SqlObjectPlugin());
        return jdbi;
    }

    @Bean
    public ProductInventoryDao productInventoryDao(Jdbi jdbi) {
        return jdbi.onDemand(ProductInventoryDao.class);
    }
}
