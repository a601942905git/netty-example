package com.netty.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * com.netty.example.IOClient
 *
 * @author lipeng
 * @date 2019-05-08 16:14
 */
public class BIOClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        // 创建socket连接
        Socket socket = new Socket("127.0.0.1", 8000);
        // 创建写对象
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        // 创建读对象
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // 循环往服务器端写数据，并从服务器端读取数据
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            writer.println("hello，i am bio client......");
            System.err.println("receive content from bio server：" + reader.readLine());
        }
    }
}
