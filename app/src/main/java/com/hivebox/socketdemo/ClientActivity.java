package com.hivebox.socketdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hivebox.socketdemo.client.Client;

public class ClientActivity extends AppCompatActivity {

    private Client mClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        mClient = new Client();

        Button startConnectBtn = findViewById(R.id.start_connect);
        startConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClient.connect();
            }
        });

        Button sendMsgBtn = findViewById(R.id.send_message);
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClient.sendMessage(Constant.START_TASK);
            }
        });
    }
}
