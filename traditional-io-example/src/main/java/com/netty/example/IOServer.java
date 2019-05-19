package com.netty.example;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * com.netty.example.ServerSocketTest
 *
 * @author lipeng
 * @date 2019-05-08 16:07
 */
public class IOServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);

        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    new Thread(() -> {
                        InputStream inputStream;
                        byte[] data = new byte[1024];
                        try {
                            inputStream = socket.getInputStream();
                            while (true) {
                                int len;
                                while ((len = inputStream.read(data, 0, data.length)) != 0) {
                                    System.out.println("服务端读取数据======>" + new String(data, 0 ,len));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
