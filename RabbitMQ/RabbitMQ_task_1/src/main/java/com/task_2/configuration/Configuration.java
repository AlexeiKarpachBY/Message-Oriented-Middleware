package com.task_2.configuration;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Value("${queue.name}")
    private String queue;

    @Value("${topic.name}")
    private String topic;

    @Value("${routingKey.name}")
    private String routingKey;

    @Value("${queue.name.failed}")
    private String failedQueue;

    @Value("${topic.name.failed}")
    private String failedTopic;

    @Value("${routingKey.name.failed}")
    private String failedRoutingKey;

    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    @Bean
    public Queue failedQueue() {
        return new Queue(failedQueue);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topic);
    }

    @Bean
    public TopicExchange failedTopicExchange() {
        return new TopicExchange(failedTopic);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(topicExchange())
                .with(routingKey);
    }

    @Bean
    public Binding failedBinding() {
        return BindingBuilder
                .bind(failedQueue())
                .to(failedTopicExchange())
                .with(failedRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
