package pl.lodz.p.library.adapters.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String USER_CREATED_EVENT_TOPIC = "user-created-topic";
    public static final String USER_CREATED_EVENT_QUEUE = "user-created-queue";
    public static final String USER_CREATED_KEY = "user-created-key";

    @Bean
    public TopicExchange userCreatedExchange() {
        return new TopicExchange(USER_CREATED_EVENT_TOPIC);
    }

    @Bean
    public Queue userCreatedQueue() {
        return new Queue(USER_CREATED_EVENT_QUEUE);
    }

    @Bean
    public Binding userCreatedBinding(Queue userCreatedQueue, TopicExchange userCreatedExchange) {
        return BindingBuilder.bind(userCreatedQueue).to(userCreatedExchange).with(USER_CREATED_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}