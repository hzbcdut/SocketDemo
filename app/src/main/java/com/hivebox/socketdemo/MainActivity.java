package com.hivebox.socketdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button clientBtn = findViewById(R.id.btn_client);
        clientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ClientActivity.class);
                startActivity(intent);
            }
        });

        Button serverBtn = findViewById(R.id.btn_server);
        serverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ServerActivity.class);
//                startActivity(intent);

                testOkio();
            }
        });

        requestReadPerssion();
    }

    // 申请读文件权限，需要测试读取模拟器中的文件进行传输
    public void requestReadPerssion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请拍照 和 读文件权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        } else {
            // todo 已经有此权限了，可以做点什么
        }
    }

    public void testOkio() {
        ByteString byteString = ByteString.decodeHex("89504e470d0a1a0a");
        String hex = byteString.hex();
        int size = byteString.size();
        Log.i("debug", TAG + " --> byteString = " + byteString + " hex = " + hex + "  size = " + size);

        for (int i = 0; i < byteString.size(); i ++) {
            Log.i("debug", TAG + " 字节  i = " + i + "  byte = " + byteString.getByte(i));

        }
    }
}
