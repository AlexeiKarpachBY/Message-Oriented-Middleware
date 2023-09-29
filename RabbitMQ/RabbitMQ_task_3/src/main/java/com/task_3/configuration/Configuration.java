package com.task_3.configuration;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.RetryException;
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

    @Value("${queue.name.dead}")
    private String deadQueue;

    @Value("${topic.name.dead}")
    private String deadTopic;

    @Value("${routingKey.name.dead}")
    private String deadRoutingKey;

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(queue)
                .withArgument("x-dead-letter-exchange", deadTopic)
                .withArgument("x-dead-letter-routing-key", deadRoutingKey)
                //.withArgument("x-message-ttl", 2000)
                .build();
    }

    @Bean
    public Queue failedQueue() {
        return new Queue(failedQueue);
    }

    @Bean
    public Queue deadQueue() {
        return new Queue(deadQueue, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(topic);
    }

    @Bean
    public DirectExchange failedExchange() {
        return new DirectExchange(failedTopic, true, false);
    }

    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(deadTopic);
    }

    @Bean
    public Binding failedBinding() {
        return BindingBuilder.bind(failedQueue()).to(failedExchange()).with(failedRoutingKey);
    }

    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(deadRoutingKey);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    RetryOperationsInterceptor interceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(5)
                .backOffOptions(300, 2, 600)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean()
    public SimpleRabbitListenerContainerFactory customConnectionFactory(CachingConnectionFactory connectionFactory) {
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        connectionFactory.setHost("localhost");
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(interceptor());
        factory.setMessageConverter(converter());
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        return new CachingConnectionFactory();
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
