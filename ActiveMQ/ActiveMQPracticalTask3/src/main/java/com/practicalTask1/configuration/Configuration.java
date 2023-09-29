package com.practicalTask1.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Topic;

@EnableJms
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Autowired
    private Environment env;

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

}
