package pl.lodz.p.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@Configuration
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ApplicationConfig {

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}