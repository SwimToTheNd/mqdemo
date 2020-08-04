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
 * 联合使用 Qos 和 Acknowledge 就可以做到按照每个消费的能力分配消息
 * <p/>
 * 轮询分发：RabbitMQ将逐个发送消息到在序列中的下一个消费者(而不考虑每个任务的时长等等，且是提前一次性分配，并非一个一个分配
 * 平均每个消费者获得相同数量的消息。这种方式分发消息机制称为Round-Robin（轮询）
 * <p/>
 * 公平分发 ：使用basicQos( prefetchCount = 1)方法，来限制RabbitMQ只发不超过1条的消息给同一个消费者。
 * 当消息处理完毕后，有了反馈，才会进行第二次发送。使用公平分发，必须关闭自动应答，改为手动应答。
 * <p>
 * channel.basicQos(1);
 * channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
 * channel.basicConsume(QUEUE_NAME, false, consumer);
 * </p>
 * create by BloodFly at 2019/3/28
 */
public class WorkerConsumer1 {
    private static final String QUEUE_NAME = "test_queue_work";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 同一时刻服务器只会发一条消息给消费者
        // Qos 的 prefetchCount 参数就是用来限制这批未确认消息数量的
        channel.basicQos(1);

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String msg = new String(message.getBody(), "utf-8");
                System.out.println(" [*] WorkerConsumer1 Received message: " + msg);
                try {
                    TimeUnit.MILLISECONDS.sleep(1000); //休眠1毫秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [*] WorkerConsumer1 done");
                    // 返回确认状态，注释掉表示使用自动确认模式
                    // 使用公平分发
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                }

            }
        };

        // autoAck: true 自动确认模式，false 手动确认模式，异步反馈消息消费情况
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });
    }
}
