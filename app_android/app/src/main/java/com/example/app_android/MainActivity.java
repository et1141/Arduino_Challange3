package com.example.app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    private static final String BROCKER_URL = "tcp://broker.hivemq.com:1883";//"tcp://mqtt-dashboard.com:8884"
    private static final String CLIENT_ID = "et1141";
    private MqttHandler mqttHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView subscribeTextView = (TextView) findViewById(R.id.TXT_topic_listener);
        subscribeTextView.setText("ASFFSAFFSA");
        callback = (new MqttCallback() {
            public void connectionLost(Throwable cause) {
            }

            public void messageArrived(String topic, MqttMessage message) throws Exception {
                CharSequence text = message.toString();

                Log.v("asd", "message set");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROCKER_URL,CLIENT_ID);
        subscribeToTopic("ESP/data");
    }
    public static MqttCallback callback;
    @Override
    protected void onDestroy(){
        mqttHandler.disconnect();
        super.onDestroy();
    }

    private void publishMessage(String topic, String message){
        Toast.makeText(this,"Publishing message: " + message, Toast.LENGTH_SHORT).show();
        mqttHandler.publish(topic,message);
    }

    private void subscribeToTopic(String topic){
        Toast.makeText(this,"Subscribe to topic "+topic,Toast.LENGTH_SHORT).show();
        mqttHandler.subscribe(topic);
    }
    public void switch_light_on(View v) {
        publishMessage("light","ON");
    }
    public void switch_light_off(View v) {
        publishMessage("light","OFF");
    }
}