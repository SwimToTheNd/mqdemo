package com.stte.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者发送消息到消息队列
 * create by BloodFly at 2019/3/28
 */
public class QueueProducer {

    private static final String QUEUE_NAME = "q_test_01";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
//        channel.txSelect();
//        channel.confirmSelect();  //开启确认模式
//        channel.addConfirmListener(ackCallback,nacCallBack); //监听成功和失败的结果，根据具体结果对消息进行重新发送或记录日志处理等后续操作
        // 声明创建队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 消息内容
        String message = "This message send to queue";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("send to mq: " + message);
        channel.close();
        connection.close();
    }
}
