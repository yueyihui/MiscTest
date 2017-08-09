package com.example.lyue.misctest;

import android.net.wifi.MyWifiManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

//执行sdk platform-tools下的dx --dex --output=dex.jar '/home/lyue/Misc-Test/test.jar'

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadJar("/storage/emulated/0/HomePanel/dex.jar");
        new MyWifiManager((WifiManager) getSystemService(WIFI_SERVICE),1);
    }

    public void loadJar(String jarPath) {
        File jarOpt = this.getDir("jarOpt", MODE_PRIVATE);
        Log.d(TAG, "loadJar: " + jarOpt.getAbsolutePath());
        final DexClassLoader classloader = new DexClassLoader(
                jarPath,
                jarOpt.getAbsolutePath(),
                null,
                this.getClassLoader());
        try {
            Class<?> classToLoad = classloader
                    .loadClass("com.example.lyue.misctest.TestClassLoader");
            Object instance = classToLoad.newInstance();
            Method method = classToLoad.getMethod("show");
            method.invoke(instance);
        } catch (IllegalAccessException
                | ClassNotFoundException
                | InvocationTargetException
                | NoSuchMethodException
                | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
