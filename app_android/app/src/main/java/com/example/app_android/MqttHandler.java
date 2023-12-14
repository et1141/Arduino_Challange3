package com.example.app_android;
//code source: https://www.youtube.com/watch?v=2ucv0IZgT1E&ab_channel=CoffeeProgrammer
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.exceptions.CsvException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class MqttHandler {

    private MqttClient client;

    public void connect(String brokerUrl, String clientId) {
        try {
            // Set up the persistence layer
            MemoryPersistence persistence = new MemoryPersistence();

            // Initialize the MQTT client
            client = new MqttClient(brokerUrl, clientId, persistence);

            // Set up the connection options
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            // Connect to the broker
            client.connect(connectOptions);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public List<List<String>> readCSVFromFile(String fileName) {
        List<List<String>> listOfLists = new ArrayList<>();
        try {
            Context context;
            File file = new File(context.getFilesDir(), fileName);
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(inputStream);

            CSVReader csvReader = new CSVReaderBuilder(reader).build();

            List<String[]> allData = csvReader.readAll();

            for (String[] row : allData) {
                listOfLists.add(new ArrayList<>(List.of(row)));
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return listOfLists;
    }

    public void writeCSVToFile(String fileName, List<List<String>> list) {
        try {
            File file = new File(context.getFilesDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);

            ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                    .withSeparator(',')
                    .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withEscapeChar(CSVWriter.NO_ESCAPE_CHARACTER)
                    .build();

            for (List<String> row : list) {
                String[] rowArray = row.toArray(new String[0]);
                csvWriter.writeNext(rowArray);
            }

            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        try {
            client.subscribe(topic);
            MqttCallback callback = new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.v("topic_msg", message.toString());

                    File file = new File(getFilesDir(),"topic_ESP_messages");

                    if (!file.exists()) {
                        try {
                            FileOutputStream fos = openFileOutput("topic_ESP_messages", Context.MODE_PRIVATE);
                            fos.write(fileContent.getBytes());
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            };

            client.setCallback(callback);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}