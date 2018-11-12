package com.madhouse.dsp.routing;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by Madhouse on 2017/9/22.
 */
public class DirectReceiver1 {
    private static final String EXCHANGE_NAME = "direct_logs";
    private static final String[] levels = {"warn", "info"};

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queue = channel.queueDeclare().getQueue();

        for (String level : levels) {
            channel.exchangeBind(queue, EXCHANGE_NAME, level);
            System.out.println("DirectReceiver1 create one routing: exchange = " + EXCHANGE_NAME + ", queue = " + queue + ", BindRoutingKey = " + level);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.out.println("for routing(" + envelope.getRoutingKey() + "): " + message);
            }
        };

        channel.basicConsume(queue, true, consumer);
    }
}
