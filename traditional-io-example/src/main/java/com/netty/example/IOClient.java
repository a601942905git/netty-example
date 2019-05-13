package com.netty.example;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * com.netty.example.IOClient
 *
 * @author lipeng
 * @date 2019-05-08 16:14
 */
public class IOClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8000);
        new Thread(() -> {
            try {
                OutputStream outputStream = socket.getOutputStream();
                while (true) {
                    outputStream.write("hello netty".getBytes());
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
