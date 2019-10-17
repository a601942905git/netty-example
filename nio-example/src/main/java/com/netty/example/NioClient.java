package com.netty.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * com.netty.example.NioClient
 * Nio客户端实现
 *
 * @author lipeng
 * @date 2019/10/16 下午7:23
 */
public class NioClient {

    public static void main(String[] args) throws IOException {
        new NioClient().start();
    }

    private void start() throws IOException {
        // 与服务器端建立连接
        SocketChannel socketChannel =
                SocketChannel.open(new InetSocketAddress("127.0.0.1", 8000));
        System.out.println("客户端与服务器端成功建立连接......");

        // 接收服务器端返回的数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String response = "";
        while (socketChannel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            response += Charset.forName("UTF-8").decode(byteBuffer);
        }
        System.out.println("客户端与服务器端建立连接，服务器端响应数据：" + response);


        // 向服务器端发送请求数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            socketChannel.write(Charset.forName("UTF-8").encode(scanner.nextLine()));
        }
    }
}
