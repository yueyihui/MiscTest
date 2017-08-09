package com.example.lyue.misctest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;

import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class EthernetService extends Service {
    private static final String TAG = EthernetService.class.getSimpleName();
    private Writer mEtherWriter;
    private BroadcastReceiver receiver;
    private ConnectivityManager cm;
    private AtomicInteger i = new AtomicInteger();
    private Socket socket;
    private Handler handler = new Handler();
    public EthernetService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*new Thread() {
            @Override
            public void run() {
                try {
                    boolean ret = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))
                            .requestRouteToHost(ConnectivityManager.TYPE_ETHERNET, lookupHost("159.99.249.140"));
                    ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))
                            .requestRouteToHost(ConnectivityManager.TYPE_ETHERNET, lookupHost("159.99.249.179"));
                    if (!ret) {
                        Log.e(TAG, "request route failed");
                        return ;
                    }
                    socket = new Socket();
                    socket.connect(new InetSocketAddress("159.99.249.140", 1883));
                    int index;
                    while((index = i.getAndIncrement()) < 100) {
                        mEtherWriter = new OutputStreamWriter(socket.getOutputStream());
                        mEtherWriter.write("is ethernet" + index + "\n");
                        mEtherWriter.flush();
                        Thread.sleep(3000);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(receiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
