package pl.lodz.p.library.adapters.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "library-system-exchange";
    public static final String USER_CREATED_QUEUE = "user-created-queue";
    public static final String USER_CREATED_ROUTING_KEY = "user.created.key";

    public static final String USER_COMPENSATION_ROUTING_KEY = "user.compensation.key";

    @Bean
    public TopicExchange systemExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue userCreatedQueue() {
        return new Queue(USER_CREATED_QUEUE, true);
    }

    @Bean
    public Binding userCreatedBinding(Queue userCreatedQueue, TopicExchange systemExchange) {
        return BindingBuilder.bind(userCreatedQueue).to(systemExchange).with(USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}