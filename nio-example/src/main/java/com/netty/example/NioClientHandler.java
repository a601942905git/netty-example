package com.netty.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * com.netty.example.NioClientHandler
 *
 * @author lipeng
 * @date 2019/10/17 下午4:54
 */
public class NioClientHandler implements Runnable {

    private Selector selector;

    public NioClientHandler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            // 此部分逻辑可参考服务器端读取数据的逻辑
            for (; ; ) {
                int readyCount = selector.select();
                if (readyCount == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                String response = "";
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        socketChannel.configureBlocking(false);
                        while (socketChannel.read(byteBuffer) > 0) {
                            byteBuffer.flip();
                            response += Charset.forName("UTF-8").decode(byteBuffer);
                            System.out.println(response);
                        }
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
