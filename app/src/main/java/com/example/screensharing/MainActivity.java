package com.example.screensharing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        EditText input_ip_address = (EditText) findViewById(R.id.editTextText);
        EditText input_port_number = (EditText) findViewById(R.id.editTextNumber);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                input_port_number.setText(input_ip_address.getText());
            }
        });
    }

}