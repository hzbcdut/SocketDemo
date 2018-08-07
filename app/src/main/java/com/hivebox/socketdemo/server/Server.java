package com.hivebox.socketdemo.server;

import android.text.TextUtils;
import android.util.Log;

import com.hivebox.socketdemo.Constant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hans on 2018/8/7.
 */
public class Server {
    private static final int PORT = 5000;// 端口号
    private static final String TAG = Server.class.getSimpleName();

    private ServerSocket mServerSocket;

    private ExecutorService mThreadPool;

    public Server(){
        mThreadPool = Executors.newCachedThreadPool();
    }

    public void connect() {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建ServerSocket并监听端口
                    mServerSocket = new ServerSocket(PORT);
                    while (true){
                        // 开始接收客户端连接
                        Socket client = mServerSocket.accept();
                        Log.i("debug", TAG + " --> Server端建立连接");
                        handleClient(client);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private PrintWriter printWriter;
    private BufferedReader  in;
    private String receiveMsg;

    BufferedWriter bufferedWriter = null;

    private void handleClient(Socket socket) {
        System.out.println("handleClient");
        // 使用socket进行通信
        try {
            // 处理发送消息
            printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream(), "UTF-8")), true);

            //处理接收消息
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//            receiveMsg();


            String message = "";
            while ((message = in.readLine()) != null) {
                System.out.println("来自客户端【" + socket.getInetAddress().getHostAddress() + "】说:" + message);

                if (!TextUtils.isEmpty(message)&&message.equals(Constant.START_TASK)) {
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 服务端发送消息给客户端
                    bufferedWriter.write(Constant.TASK_FINISHED);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    System.out.println("给客户端发一个消息");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMsg() {
        try {
            while (true) {
                if ((receiveMsg = in.readLine()) != null) {
                    Log.d(TAG, "receiveMsg:" + receiveMsg);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "receiveMsg: ");
            e.printStackTrace();
        }
    }

}
