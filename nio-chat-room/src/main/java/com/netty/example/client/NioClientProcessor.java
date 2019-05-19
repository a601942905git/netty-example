package com.netty.example.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * com.netty.example.client.NioClientProcessor
 *
 * @author lipeng
 * @date 2019-05-19 17:47
 */
public class NioClientProcessor {

    private String nickName;

    public NioClientProcessor(String nickName) {
        this.nickName = nickName;
    }

    public void start() throws IOException {
        // 1. 连接服务器端
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8000));
        System.out.println("客户端启动成功......");

        // 2. 接受服务器端响应数据
        Selector selector = Selector.open();
        // 设置为非阻塞模式
        socketChannel.configureBlocking(false);
        // 将SocketChannel注册到Selector上，并监听读请求
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 启动线程来接受服务器端响应的数据
        NioClientReceiveDataHandler nioClientReceiveDataHandler = new NioClientReceiveDataHandler(selector);
        new Thread(nioClientReceiveDataHandler).start();

        // 3. 向服务器端发送请求数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            socketChannel.write(Charset.forName("UTF-8").encode(nickName + ":" + scanner.nextLine()));
        }
    }
}
