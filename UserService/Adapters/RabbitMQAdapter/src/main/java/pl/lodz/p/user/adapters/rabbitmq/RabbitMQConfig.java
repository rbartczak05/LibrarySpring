package pl.lodz.p.user.adapters.rabbitmq;

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
    public static final String USER_CREATED_EVENT_QUEUE = "user-created-event-queue";
    public static final String USER_COMPENSATION_KEY = "user.compensation.key";

    @Bean
    public TopicExchange userCreatedEventsTopicExchange() {
        return new TopicExchange(USER_CREATED_EVENT_TOPIC);
    }

    @Bean
    public Queue userCompensationQueue() {
        return new Queue(USER_CREATED_EVENT_QUEUE, true);
    }

    @Bean
    public Binding compensationBinding(Queue userCompensationQueue, TopicExchange userCreatedEventsTopicExchange) {
        return BindingBuilder.bind(userCompensationQueue).to(userCreatedEventsTopicExchange).with(USER_CREATED_EVENT_QUEUE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}