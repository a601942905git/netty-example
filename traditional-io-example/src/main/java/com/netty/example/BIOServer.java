package com.netty.example;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * com.netty.example.ServerSocketTest
 *
 * @author lipeng
 * @date 2019-05-08 16:07
 */
public class BIOServer implements Runnable {

    private Socket socket;

    public BIOServer(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) throws IOException {
        // 创建ServerSocket对象
        ServerSocket serverSocket = new ServerSocket(8000);
        // 循环监听端口
        while (true) {
            try {
                // 接收连接
                Socket socket = serverSocket.accept();
                // 每个连接创建一个线程进行处理
                new Thread(new BIOServer(socket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            // 创建读对象
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 创建写对象
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            // 循环从客户端读取数据，并往客户端写数据
            while (true) {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("receive content from bio client：" + reader.readLine());
                writer.println("hello，i am bio server......");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
