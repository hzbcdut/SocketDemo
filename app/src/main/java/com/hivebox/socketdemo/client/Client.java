package com.hivebox.socketdemo.client;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hans on 2018/8/7.
 */
public class Client {
    public static final String TAG = Client.class.getSimpleName();

    private Socket mSocket;
    private ExecutorService mThreadPool;

    private static final String  HOST = "10.0.2.2";
    private static final int PORT = 5000;

    public Client(){
        mThreadPool = Executors.newCachedThreadPool();
    }

    public void connect() {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(HOST, PORT);
                    Log.i("debug", TAG + " --> 连接状态 mSocket.isConnected() = " + mSocket.isConnected());

                    try {
                        while (true) {
                            BufferedReader bufferedReader_Server = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                            String message;
                            while ((message = bufferedReader_Server.readLine()) != null) {
                                System.out.println("来自服务器说：" + message);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("debug", TAG + " --> 连接状态 e = " + e);
                }
            }
        });
    }

    public void sendMessage(String msg) {
        if (mSocket != null && mSocket.isConnected()) {
            try {
                //socket输出流。将信息数据输出给服务端
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                bufferedWriter.write(msg);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
