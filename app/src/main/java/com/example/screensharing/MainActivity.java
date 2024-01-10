package com.example.screensharing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    // Views
    private EditText ipAddressInput;
    private EditText portNumberInput;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get views
        ipAddressInput = (EditText) findViewById(R.id.editTextText);
        portNumberInput = (EditText) findViewById(R.id.editTextNumber);
        button = (Button) findViewById(R.id.button);

        // Button press event listener
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Get user inputs
                String ipAddress = ipAddressInput.getText().toString();
                int port = Integer.parseInt(portNumberInput.getText().toString());

                // start the Thread to connect to server
                new Thread(new ClientThread("message", ipAddress, port)).start();
            }
        });
    }
    // the ClientThread class performs the networking operations
    class ClientThread implements Runnable {

        private final String message;
        private final String ipAddress;
        private final int port;

        ClientThread(String message, String ipAddress, int port) {
            this.message = message;
            this.ipAddress = ipAddress;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                Socket client = new Socket(ipAddress, port);  // connect to server
                PrintWriter printwriter = new PrintWriter(client.getOutputStream(),true);
                printwriter.write(message);  // write the message to output stream

                printwriter.flush();
                printwriter.close();

                // closing the connection
                client.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}