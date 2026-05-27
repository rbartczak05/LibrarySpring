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
    public static final String USER_CREATED_EXCHANGE = "user-created-exchange";
    public static final String USER_CREATED_QUEUE    = "user-created-queue";
    public static final String USER_CREATED_KEY      = "user.created";

    public static final String USER_COMPENSATE_EXCHANGE    = "user-compensate-exchange";
    public static final String USER_COMPENSATE_QUEUE       = "user-compensate-queue";
    public static final String USER_COMPENSATE_ROUTING_KEY = "user.compensate";

    @Bean
    public TopicExchange userCreatedExchange() {
        return new TopicExchange(USER_CREATED_EXCHANGE);
    }

    @Bean
    public Queue userCreatedQueue() {
        return new Queue(USER_CREATED_QUEUE, true);
    }

    @Bean
    public Binding userCreatedBinding(Queue userCreatedQueue, TopicExchange userCreatedExchange) {
        return BindingBuilder.bind(userCreatedQueue).to(userCreatedExchange).with(USER_CREATED_KEY);
    }

    @Bean
    public TopicExchange userCompensateExchange() {
        return new TopicExchange(USER_COMPENSATE_EXCHANGE);
    }

    @Bean
    public Queue userCompensateQueue() {
        return new Queue(USER_COMPENSATE_QUEUE, true);
    }

    @Bean
    public Binding userCompensateBinding(Queue userCompensateQueue, TopicExchange userCompensateExchange) {
        return BindingBuilder.bind(userCompensateQueue).to(userCompensateExchange).with(USER_COMPENSATE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}