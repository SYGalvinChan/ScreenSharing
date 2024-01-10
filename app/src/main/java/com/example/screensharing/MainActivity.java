package com.example.screensharing;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.Cursor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    // Views
    private EditText ipAddressInput;
    private EditText portNumberInput;
    private Button button;
    String ipAddress;
    int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get views
        ipAddressInput = (EditText) findViewById(R.id.editTextText);
        portNumberInput = (EditText) findViewById(R.id.editTextNumber);
        button = (Button) findViewById(R.id.button);

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);

                        Cursor returnCursor =
                                getContentResolver().query(uri, null, null, null, null);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        byte[] arr = new byte[returnCursor.getInt(sizeIndex)];
                        inputStream.read(arr);
                        new Thread(new ClientThread(arr, ipAddress, port)).start();
                    } catch (FileNotFoundException e) {
                        ipAddressInput.setText("File not found");
                    } catch (IOException e) {
                        ipAddressInput.setText("IO exception");
                    }

                });


        // Button press event listener
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Get user inputs
                ipAddress = ipAddressInput.getText().toString();
                port = Integer.parseInt(portNumberInput.getText().toString());
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });
    }
    // the ClientThread class performs the networking operations
    class ClientThread implements Runnable {

        private final byte[] message;
        private final String ipAddress;
        private final int port;

        ClientThread(byte[] message, String ipAddress, int port) {
            this.message = message;
            this.ipAddress = ipAddress;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                Socket client = new Socket(ipAddress, port);  // connect to server
                OutputStream outputStream = client.getOutputStream();
                outputStream.write(message);  // write the message to output stream

                outputStream.flush();
                outputStream.close();

                // closing the connection
                client.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}