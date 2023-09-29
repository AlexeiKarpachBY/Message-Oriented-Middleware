package com.task_2.configuration;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.util.HashMap;
import java.util.Map;

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
    public TopicExchange exchange() {
        return new TopicExchange(topic);
    }


    @Bean
    public DirectExchange failedExchange() {
        return new DirectExchange(failedTopic, true, false);
    }

    @Bean
    public Binding failedBinding() {
        return BindingBuilder.bind(failedQueue()).to(failedExchange()).with(failedRoutingKey);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public SimpleRetryPolicy rejectionRetryPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptionsMap = new HashMap<>();
        exceptionsMap.put(Exception.class, true);
        return new SimpleRetryPolicy(3, exceptionsMap, true);
    }

    @Bean
    RetryOperationsInterceptor interceptor() {
        return RetryInterceptorBuilder.stateless()
                .retryPolicy(rejectionRetryPolicy())
                .backOffOptions(2000L, 2, 3000L)
                .recoverer(new RepublishMessageRecoverer(rabbitTemplate(), failedTopic, failedRoutingKey))
                .build();
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean()
    SimpleRabbitListenerContainerFactory customConnectionfactory(CachingConnectionFactory connectionFactory) {
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        connectionFactory.setHost("localhost");
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(interceptor());
        factory.setMessageConverter(converter());
        return factory;
    }

    @Bean
    CachingConnectionFactory connectionFactory() {
        return new CachingConnectionFactory();
    }

    @Bean
    RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
