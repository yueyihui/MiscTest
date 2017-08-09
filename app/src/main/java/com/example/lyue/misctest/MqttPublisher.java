package com.example.lyue.misctest;


import com.honeywell.iot.sdk.mqtt.MqttConnector;

/**
 * Created by lyue on 17-4-24.
 */

public class MqttPublisher {
    private MqttConnector mMqttConnector;
    public MqttPublisher() throws Exception {
        mMqttConnector = new MqttConnector();
        mMqttConnector.init("tcp://115.159.81.105:1883", "0002");
    }

    public void sendMessage(String topic, String content) {
        int code = mMqttConnector.sentMessage(topic, content);
        if (code == 0) {
            System.out.println("sent msg success");
        } else {
            System.out.println("sent msg failed");
        }
    }

    public void close () {
        mMqttConnector.close();
    }
}
