package com.stte.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * work模式消费者
 * create by BloodFly at 2019/3/28
 */
public class WorkerConsumer2 {
    private static final String QUEUE_NAME = "test_queue_work";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String msg = new String(message.getBody(), "utf-8");
                System.out.println(" [*] WorkerConsumer2 Received message: " + msg);
                try {
                    TimeUnit.MILLISECONDS.sleep(5000); //休眠3秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [*] WorkerConsumer2 done");
                    // 返回确认状态，注释掉表示使用自动确认模式
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                }

            }
        };
        // autoAck: true 自动确认模式，false 手动确认模式
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});
    }
}
