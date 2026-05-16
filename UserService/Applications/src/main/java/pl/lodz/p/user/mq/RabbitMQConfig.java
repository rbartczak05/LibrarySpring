package pl.lodz.p.user.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "library.exchange";
    public static final String READER_CREATED_QUEUE = "reader.created.queue";
    public static final String READER_ROUTING_KEY = "reader.created";

    @Bean
    public DirectExchange libraryExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue readerCreatedQueue() {
        return new Queue(READER_CREATED_QUEUE, true);
    }

    @Bean
    public Binding bindingReaderCreated(Queue readerCreatedQueue, DirectExchange libraryExchange) {
        return BindingBuilder.bind(readerCreatedQueue).to(libraryExchange).with(READER_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter producerMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}