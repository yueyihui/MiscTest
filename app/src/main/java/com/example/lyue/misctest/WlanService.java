package com.example.lyue.misctest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class WlanService extends Service {
    private static final String TAG = WlanService.class.getSimpleName();
    private Writer mWlanWriter;
    private ConnectivityManager cm;
    private Handler handler = new Handler();
    private BroadcastReceiver receiver;
    private AtomicInteger i = new AtomicInteger();
    private Socket socket;
    public WlanService() {
    }


    class ReadThread extends Thread{
        private  Socket mSocket = null;
        public ReadThread(Socket mSocket) {
            this.mSocket = mSocket;
        }

        @Override
        public void run() {
            char[] buffer = new char[256];
            while (true){
                try{
                    InputStreamReader reader = new InputStreamReader(mSocket.getInputStream());
                    while (true) {
                        int count = reader.read(buffer);
                        Log.d(TAG, "accept = " + String.valueOf(buffer));
                        Log.d(TAG, "count = " + count);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                ;
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        NetworkRequest.Builder request = new NetworkRequest.Builder();
        request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        cm = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE));
        cm.requestNetwork(request.build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket();
                            socket.connect(new InetSocketAddress("192.168.1.104", 1883));
                            int index;
                            while((index = i.getAndIncrement()) < 100) {
                                mWlanWriter = new OutputStreamWriter(socket.getOutputStream());
                                mWlanWriter.write("is wifi" + index + "\n");
                                mWlanWriter.flush();
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
