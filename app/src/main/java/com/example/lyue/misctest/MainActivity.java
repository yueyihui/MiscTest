package com.example.lyue.misctest;

import android.content.Intent;
import android.hardware.display.DisplayManagerGlobal;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MqttSubscribtor subscribtor;
    private MqttPublisher publisher;
    private Thread testStopThread;
    private Writer mEtherWriter;
    private Writer mWlanWriter;
    private ConnectivityManager cm;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayManagerGlobal.getInstance().requestScreenOff(true);
                /*try {
                    subscribtor = new MqttSubscribtor(MainActivity.this, false);
                    subscribtor.subscribeTopic(subscribtor.getConnector(),new IotCallback() {

                        @Override
                        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                            Log.d(TAG, "messageArrived: " + mqttMessage.toString());
                        }
                    }, "/hi");
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });

        findViewById(R.id.baba).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayManagerGlobal.getInstance().requestScreenOff(false);
            }
        });

        final FloatingActionButton b = (FloatingActionButton) findViewById(R.id.fab2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))
                        .requestRouteToHost(ConnectivityManager.TYPE_ETHERNET
                                , lookupHost("159.99.249.140"));
            }
        });
        /*try {
            URL url1 = new URL("file:/storage/emulated/legacy/HomePanel");
            URLClassLoader loader = new URLClassLoader(
                    new URL[] { url1 }, Thread.currentThread().getContextClassLoader());
            Class clazz = loader.loadClass("com.example.lyue.mqtt_test.TestClassLoader");
            Object obj = clazz.newInstance();
            clazz.getDeclaredMethods()[0].invoke(obj);
        } catch (MalformedURLException | IllegalAccessException | ClassNotFoundException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }

        testStopThread = new Thread("yueliang") {
            @Override
            public void run() {
                String importantInfo[] = {
                        "Mares eat oats",
                        "Does eat oats",
                        "Little lambs eat ivy",
                        "A kid will eat ivy too"
                };
                try {
                    for (String anImportantInfo : importantInfo) {
                        Thread.sleep(20*1000);
                        threadMessage(anImportantInfo);
                    }
                } catch (InterruptedException e) {
                    threadMessage("I wasn't done!");
                }

                for (String anImportantInfo : importantInfo) {
                    threadMessage(anImportantInfo);
                }
            }
        };
        testStopThread.start();*/
        /*EthernetManager ethManager = EthernetManager.getInstance();
        if(ethManager == null) {
            Log.d(TAG, "haven't got ethernetManager");
        } else {
            Log.d(TAG, "we got ethernetManager !");
            ethManager.setEthernetEnabled(true);//开启以太网
            EthernetDevInfo devInfo = ethManager.getStaticConfig();
            if (devInfo.getIpAddress() == null) {
                devInfo = ethManager.getDevInfo();
            }
            devInfo.setMode(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL);
            devInfo.setIpAddress("159.99.249.141");
            devInfo.setNetMask("255.255.255.0");
            devInfo.setGateWay("159.99.249.140");
            ethManager.setEthernetMode(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL, devInfo);//设置为静态模式

            ethManager.setDefaultConf();//设置为dhcp模式

        *//* Get the SaveConfig and update for Dialog. *//*
            EthernetDevInfo ethDevInfo = ethManager.getDevInfo();
            String currentMode = ethManager.getEthernetMode();
            ethDevInfo.getHwaddr();//mac地址
            ethDevInfo.getMacAddress();//mac地址 ,这个和上面的都行
            ethDevInfo.getIpAddress();
            ethDevInfo.getNetMask();
            ethDevInfo.getGateWay();
            // currentMode is EthernetManager.ETHERNET_CONNECT_MODE_DHCP;
            // or EthernetManager.ETHERNET_CONNECT_MODE_MANUAL;
            // or EthernetManager.ETHERNET_CONNECT_MODE_PPPOE;
        }*/
        startService(new Intent(this, EthernetService.class));
        startService(new Intent(this, WlanService.class));
    }
    public static int lookupHost(String hostname) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            return -1;
        }
        byte[] addrBytes;
        int addr;
        addrBytes = inetAddress.getAddress();
        addr = ((addrBytes[3] & 0xff) << 24)
                | ((addrBytes[2] & 0xff) << 16)
                | ((addrBytes[1] & 0xff) << 8)
                |  (addrBytes[0] & 0xff);
        return addr;
    }
    private void threadMessage(String message) {
        String threadName =
                Thread.currentThread().getName();
        System.out.format("%s: %s%n",
                threadName,
                message);
    }

    private void stopThread() {
        testStopThread.interrupt();
        try {
            testStopThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribtor != null) {
            subscribtor.close();
        }
        if (publisher != null) {
            publisher.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            stopThread();
            return true;
        }

        if (id == R.id.ethernet) {
            if (mEtherWriter != null) {
                try {
                    mEtherWriter.write("is ethernet\n");
                    mEtherWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }

        if (id == R.id.wifi) {
            if (mWlanWriter != null) {
                try {
                    mWlanWriter.write("is wifi\n");
                    mWlanWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
