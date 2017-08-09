package com.example.lyue.misctest;

import android.content.Context;
import android.util.Log;
import com.honeywell.iot.sdk.mqtt.IotCallback;
import com.honeywell.iot.sdk.mqtt.MqttConnector;

/**
 * Created by lyue on 17-4-24.
 */

public class MqttSubscribtor {
    private static final String TAG = "MqttSubscribtor";
    private SslConnector sslConnector;
    private MqttConnector tcpConnector;

    public MqttSubscribtor(Context context, boolean isSSL) throws Exception {
        if (isSSL) {
            /*sslConnector = new SslConnector();
            code = sslConnector.init(context
                    , "cacert.pem"
                    , "cert.pem"
                    , "key.pem"
                    , ""
                    , "ssl://115.159.81.105:8883"
                    , "0001");
            if (code == 0) {
                Log.d(TAG, "SSL init success");
            } else {
                throw new Exception("SSL init failed");
            }*/
        } else {
            tcpConnector = new MqttConnector();
            int code = tcpConnector.init("tcp://192.168.1.106:1883"/*"tcp://115.159.81.105:1883"*/, "0001");
            if (code != 0) {
                throw new Exception("TCP init failed");
            }
        }
    }

    public MqttConnector getConnector() {
        if (sslConnector != null) {
            return sslConnector;
        } else {
            return tcpConnector;
        }
    }

    public void subscribeTopic(MqttConnector connector
            , IotCallback iotCallback, String topic) throws Exception {
        int code = connector.subTopic(topic);
        if (code != 0) {
            throw new Exception("subscribe topic failed");
        } else {
            Log.d(TAG, "subscribe seccussful");
            connector.setReceiveMsgCallback(iotCallback);
        }
    }

    public void close() {
        if(tcpConnector != null) {
            tcpConnector.close();
        }
        if(sslConnector != null) {
            sslConnector.close();
        }
    }

    private class DeviceInfo{
        String deviceId;//is CLIENT_ID
        String deviceName;
        String deviceLoginTime;

        DeviceInfo(String deviceId,String deviceName,String deviceLoginTime){
            this.deviceId=deviceId;
            this.deviceName=deviceName;
            this.deviceLoginTime=deviceLoginTime;
        }
    }
}
