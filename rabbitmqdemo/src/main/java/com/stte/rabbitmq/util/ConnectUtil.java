package com.stte.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ连接工具类
 * create by BloodFly at 2019/3/28
 */
public class ConnectUtil {

    private static final String host = "192.168.137.83";
    private static final int port = 5672;

    public static Connection getConnection() throws IOException, TimeoutException {
        // 连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        // 获取连接
        Connection connection = connectionFactory.newConnection();
        return connection;
    }
}
