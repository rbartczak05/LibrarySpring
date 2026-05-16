package pl.lodz.p.library.mq;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "library.exchange";
    public static final String READER_CREATED_QUEUE = "reader.created.queue";
    public static final String REJECTED_ROUTING_KEY = "client.creation.rejected";

    @Bean
    public DirectExchange libraryExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue readerCreatedQueue() {
        return new Queue(READER_CREATED_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}