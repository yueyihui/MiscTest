package com.example.lyue.misctest;

import android.content.Context;
import android.content.res.AssetManager;

import com.honeywell.iot.sdk.mqtt.Config;
import com.honeywell.iot.sdk.mqtt.MqttConnector;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by lyue on 17-4-24.
 */

public class SslConnector extends MqttConnector {
    private MqttClient client;
    private AssetManager mAssetManager;

    public int init(Context context, String caCrtFile, String crtFile, String keyFile, String password, String broker, String clientId) {
        mAssetManager = context.getAssets();
        return this.createClient(new Config(caCrtFile, crtFile, keyFile, password, broker, clientId));
    }

    private SSLSocketFactory getSocketFactory(String caCrtFile, String crtFile, String keyFile, final String password) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        PEMReader reader = new PEMReader(new InputStreamReader(mAssetManager.open(caCrtFile)));
        X509Certificate caCert = (X509Certificate)reader.readObject();
        reader.close();
        reader = new PEMReader(new InputStreamReader(mAssetManager.open(crtFile)));
        X509Certificate cert = (X509Certificate)reader.readObject();
        reader.close();
        reader = new PEMReader(new InputStreamReader(mAssetManager.open(keyFile)), new PasswordFinder() {
            public char[] getPassword() {
                return password.toCharArray();
            }
        });
        KeyPair key = (KeyPair)reader.readObject();
        reader.close();
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caKs);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("certificate", cert);
        ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(), new Certificate[]{cert});
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password.toCharArray());
        SSLContext context = SSLContext.getInstance("TLSv1");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return context.getSocketFactory();
    }

    private int createClient(Config config) {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions connOpts = new MqttConnectOptions();

        try {
            this.client = new MqttClient(config.getBroker(), config.getClientId(), persistence);
            if(config.getBroker().contains("ssl://")) {
                try {
                    connOpts.setSocketFactory(this.getSocketFactory(config.getCaCrtFile(), config.getCrtFile(), config.getKeyFile(), config.getKeyPassword()));
                } catch (Exception var5) {
                    return 11000;
                }
            }

            this.client.connect(connOpts);
            return 0;
        } catch (MqttException var6) {
            return var6.getReasonCode();
        }
    }
}
