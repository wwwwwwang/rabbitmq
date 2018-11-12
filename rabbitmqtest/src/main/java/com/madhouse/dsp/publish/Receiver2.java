package com.madhouse.dsp.publish;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by Madhouse on 2017/9/22.
 */
public class Receiver2 {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String queue = channel.queueDeclare().getQueue();
        channel.exchangeBind(queue,EXCHANGE_NAME,"");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };

        channel.basicConsume(queue, true, consumer);
    }
}
