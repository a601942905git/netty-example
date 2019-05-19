package com.netty.example.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * com.netty.example.client.NioClientReceiveDataHandler
 *
 * @author lipeng
 * @date 2019-05-19 20:06
 */
public class NioClientReceiveDataHandler implements Runnable{

    private Selector selector;

    public NioClientReceiveDataHandler (Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        for (; ; ) {
            // 获取可用channel数量
            int readyChannel = 0;
            try {
                readyChannel = selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (readyChannel == 0) {
                continue;
            }

            // 获取可用channel集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                iterator.remove();

                // 读操作处理
                if (selectionKey.isReadable()) {
                    try {
                        readableHandler(selectionKey);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void readableHandler(SelectionKey selectionKey) throws IOException {
        // 从SelectionKey获取就绪的SocketChannel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        // 创建ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        StringBuilder response = new StringBuilder();
        // 循环读取服务器端响应数据
        while (socketChannel.read(byteBuffer) > 0) {
            // 将ByteBuffer切换成读模式
            byteBuffer.flip();
            response.append(Charset.forName("UTF-8").decode(byteBuffer));
            System.out.println(response.toString());
        }
    }
}
