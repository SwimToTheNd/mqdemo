package com.stte.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Work模式
 * 一个生产者，多个消费者消费同一个队列，一个消息只能被一个消费者消费
 * create by BloodFly at 2019/3/28
 */
public class WorkerProducer {
    private static final String QUEUE_NAME = "test_queue_work";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        for (int i = 0; i < 100; i++) {
            String message = " worker mode message num: " + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [*] send message [" + message + "]");
            TimeUnit.MILLISECONDS.sleep(1000);
        }

        channel.close();
        connection.close();

    }
}
