package com.stte.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模式 Publish/Subscribe 一次性发送省到多个消费者
 * <p>
 * 1、1个生产者，多个消费者
 * 2、每一个消费者都有自己的一个队列
 * 3、生产者没有将消息直接发送到队列，而是发送到了交换机
 * 4、每个队列都要绑定到交换机
 * 5、生产者发送的消息，经过交换机，到达队列，实现，一个消息被多个消费者获取的目的
 * 注意：一个消费者队列可以有多个消费者实例，只有其中一个消费者实例会消费
 * 注意：消息发送到没有队列绑定的交换机时，消息将丢失，因为，交换机没有存储消息的能力，消息只能存在在队列中。
 * </p>
 * 生产者向交换机发送消息
 * create by BloodFly at 2019/3/28
 */
public class SubscribeProducer {

    private static final String EXCHANGE_NAME = "test_exchange_fanout";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明Exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        for (int i = 0; i < 20; i++) {
            // 消息内容
            String message = "this message send to exchange " + i;
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println(" [*] send [" + message + "]");
            TimeUnit.MILLISECONDS.sleep(1000);
        }

        channel.close();
        connection.close();
    }
}
