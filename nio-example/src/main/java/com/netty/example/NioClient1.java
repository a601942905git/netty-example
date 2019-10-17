package com.netty.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * com.netty.example.NioClient1
 *
 * @author lipeng
 * @date 2019/10/17 下午4:53
 */
public class NioClient1 {

    public static void main(String[] args) throws IOException {
        new NioClient1().start("");
    }

    public void start(String nickName) throws IOException {
        // 与服务器端建立连接
        SocketChannel socketChannel =
                SocketChannel.open(new InetSocketAddress("127.0.0.1", 8000));
        System.out.println("客户端与服务器端成功建立连接......");

        // 接收服务器端返回的数据
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        new Thread(new NioClientHandler(selector)).start();


        // 向服务器端发送请求数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            socketChannel.write(
                    Charset.forName("UTF-8").encode(nickName + "：" + scanner.nextLine()));
        }
    }
}
