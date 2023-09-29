package com.practicalTask1.configuration;

import com.practicalTask1.subscriber.DurableSubscriber;
import com.practicalTask1.subscriber.SimpleSubscriber;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.Topic;

@org.springframework.context.annotation.Configuration
@PropertySource(value = "classpath:application.properties")
public class Configuration {
    @Autowired
    private Environment env;

    @Autowired
    private SimpleSubscriber simpleSubscriber;

    @Autowired
    private DurableSubscriber durableSubscriber;

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(env.getProperty("JMS.BROKER.URL"));
    }

    @Bean
    public Topic topic() {
        return new ActiveMQTopic(env.getProperty("JMS.TOPIC.NAME"));
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());

        jmsTemplate.setDefaultDestination(topic());
        jmsTemplate.setPubSubDomain(true);

        return jmsTemplate;
    }

    @Bean(name = "messageListenerContainerSimple")
    public MessageListenerContainer messageListenerContainerSimple() {

        DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer();
        messageListenerContainer.setDestination(topic());
        messageListenerContainer.setMessageListener(simpleSubscriber);
        messageListenerContainer.setConnectionFactory(connectionFactory());

        return messageListenerContainer;
    }

    @Bean(name = "messageListenerContainerDurable")
    public MessageListenerContainer messageListenerContainerDurable() {

        DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer();
        messageListenerContainer.setDestination(topic());
        messageListenerContainer.setMessageListener(durableSubscriber);
        messageListenerContainer.setConnectionFactory(connectionFactory());
        messageListenerContainer.setSubscriptionDurable(true);
        messageListenerContainer.setClientId("ClientID");

        return messageListenerContainer;
    }
}
