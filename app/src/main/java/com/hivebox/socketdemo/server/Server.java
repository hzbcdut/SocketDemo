package com.hivebox.socketdemo.server;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.hivebox.socketdemo.Constant;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

import static android.os.Environment.DIRECTORY_MOVIES;

/**
 * Created by hans on 2018/8/7.
 */
public class Server {
    private static final int PORT = 5000;// 端口号
    private static final String TAG = Server.class.getSimpleName();

    private ServerSocket mServerSocket;
    private Socket socket;

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
                        socket = client;
                        Log.i("debug", TAG + " --> Server端建立连接  socket = " + socket + " client = " + client);
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
//            // 处理发送消息
//            printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
//                    socket.getOutputStream(), "UTF-8")), true);
//
//            //处理接收消息
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//            String message;
//            while ((message = in.readLine()) != null) {
//                System.out.println("来自客户端【" + socket.getInetAddress().getHostAddress() + "】说:" + message);
//
//                if (!TextUtils.isEmpty(message)&&message.equals(Constant.START_TASK)) {
//                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    // 服务端发送消息给客户端
//                    bufferedWriter.write(Constant.TASK_FINISHED);
//                    bufferedWriter.newLine();
//                    bufferedWriter.flush();
//                    System.out.println("给客户端发一个消息");
//
//
//                    // 发送视频文件
////                    sendFile(getVideoPath(), socket);
//                }
//            }


            // 使用Okio来处理流消息
            Sink sink = Okio.sink(socket.getOutputStream());
            BufferedSink bufferedSink = Okio.buffer(sink);


            Source source = Okio.source(socket.getInputStream());
            BufferedSource bufferedSource = Okio.buffer(source);
            String receiveMsg;
            while ((receiveMsg = bufferedSource.readUtf8Line()) != null) {
                System.out.println("来自客户端【" + socket.getInetAddress().getHostAddress() + "】说:" + receiveMsg);

                if (!TextUtils.isEmpty(receiveMsg)&&receiveMsg.equals(Constant.START_TASK)) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 服务端发送消息给客户端

                    byte[]  bytes = Constant.TASK_FINISHED.getBytes();
//                    bufferedSink.writeUtf8(Constant.TASK_FINISHED);
                    bufferedSink.write(bytes);
                    bufferedSink.flush();
                    System.out.println("给客户端发一个消息");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getVideoPath() {
        // 在模拟器的sdcard/Movies目录下放了一个文件用来测试
        File file = Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES);

        File  xiaosongshuFile = new File(file, "xiaosongshu.mp4");
        long length = xiaosongshuFile.length();
        String path = xiaosongshuFile.getAbsolutePath();
        Log.i("debug", TAG + " --> length = " + length + "   path = " + path);

        return path;
    }


    public void sendFile(final String filePath) {
        if (socket != null && socket.isConnected()) {
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    sendFile(filePath, socket);
                }
            });
        }
    }

    public void sendFile(String filePath,Socket socket) {
        DataOutputStream dos;
        DataInputStream dis;

        try {
            File file = new File(filePath);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
            int buffferSize = 1024;
            byte[] bufArray = new byte[buffferSize];
            dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong((long) file.length());
            dos.flush();
            while (true) {
                int read = 0;
                if (dis != null) {
                    read = dis.read(bufArray);
                }
                if (read == -1) {
                    break;
                }
                dos.write(bufArray, 0, read);
            }
            dos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 什么时候关闭好些？
            // 关闭所有连接
//            try {
//                if (dos != null)
//                    dos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                if (dis != null)
//                    dis.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                if (socket != null)
//                    socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

    }


    }
