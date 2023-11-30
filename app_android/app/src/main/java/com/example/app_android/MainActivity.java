package com.example.app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {

    private static final String BROCKER_URL = "tcp://broker.hivemq.com:1883";//"tcp://mqtt-dashboard.com:8884"
    private static final String CLIENT_ID = "et1141aaaaa";
    private MqttHandler mqttHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROCKER_URL,CLIENT_ID);
    }

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