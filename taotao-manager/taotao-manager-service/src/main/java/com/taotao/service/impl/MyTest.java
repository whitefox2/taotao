package com.taotao.service.impl;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;

import javax.jms.*;

public class MyTest {
    @Test
    public void pointSend(){
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.109.103:61616");
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("queue");
            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage("point to point");
            producer.send(message);
            producer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
