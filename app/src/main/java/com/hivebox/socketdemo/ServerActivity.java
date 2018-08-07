package com.hivebox.socketdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hivebox.socketdemo.server.Server;

public class ServerActivity extends AppCompatActivity {

    private Server mServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        mServer = new Server();

        Button startServiceBtn = findViewById(R.id.start_server);
        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mServer.connect();
            }
        });
    }
}
