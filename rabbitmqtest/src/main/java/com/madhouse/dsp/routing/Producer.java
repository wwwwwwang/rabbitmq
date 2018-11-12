package com.madhouse.dsp.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by Madhouse on 2017/9/22.
 */
public class Producer {
    private static final String EXCHANGE_NAME = "direct_logs";
    private static final String[] levels = {"warn", "info", "error"};

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        for (String level : levels) {
            String message = level + ":just test...";
            channel.basicPublish(EXCHANGE_NAME, level, null, message.getBytes());
            System.out.println("send one message to routing(" + level + "):" + message);
        }

        channel.close();
        connection.close();
    }
}
