package com.netty.example.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * com.netty.example.NioServerProcessor
 *
 * @author lipeng
 * @date 2019-05-19 16:02
 */
public class NioServerProcessor {

    public void start() throws IOException {
        // 1. 创建Selector
        Selector selector = Selector.open();

        // 2. 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 3. ServerSocketChannel绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8000));

        // 4. ServerSocketChannel设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 5. ServerSocketChannel注册到Selector上，监听连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器端启动成功......");

        // 6. 循环等待新连接的接入
        for (; ; ) {
            // 获取可用channel数量
            int readyChannel = selector.select();

            if (readyChannel == 0) {
                continue;
            }

            // 获取可用channel集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            /**
             * 遍历所有可用的SelectionKey
             * 根据不同事件类型，调用相应的处理方法
             */
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                iterator.remove();

                // 7. 根据不同的就绪状态调用相应的业务逻辑处理

                // 接收连接处理
                if (selectionKey.isAcceptable()) {
                    acceptableHandler(serverSocketChannel, selector);
                }

                // 读操作处理
                if (selectionKey.isReadable()) {
                    readableHandler(selectionKey, selector);
                }
            }
        }
    }

    /**
     * 处理连接事件
     *
     * @param serverSocketChannel
     * @param selector
     * @throws IOException
     */
    private void acceptableHandler(ServerSocketChannel serverSocketChannel, Selector selector)
            throws IOException {
        // 1. 获取SocketChannel
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 2. 设置SocketChannel为非阻塞
        socketChannel.configureBlocking(false);

        // 3. 将SocketChannel注册到Selector上，并监听读读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 4. 响应客户端
        socketChannel.write(Charset.forName("UTF-8").encode("欢迎进入聊天室，你与其它人不是好友关系，请注意个人隐私"));
    }

    /**
     * 处理读取事件
     * @param selectionKey
     * @param selector
     * @throws IOException
     */
    private void readableHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        // 1. 从SelectionKey获取就绪的SocketChannel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        // 2. 创建ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        StringBuilder request = new StringBuilder();
        // 3. 循环读取客户端请求数据
        while (socketChannel.read(byteBuffer) > 0) {
            // 将ByteBuffer切换成读模式
            byteBuffer.flip();
            request.append(Charset.forName("UTF-8").decode(byteBuffer));
        }

        // 4. 将客户端请求数据广播给其它客户端
        broadCastMessageToClient(socketChannel, selector, request.toString());
    }

    /**
     * 广播消息给其余所有客户端
     *
     * @param sourceSocketChannel
     * @param selector
     * @param request
     * @throws IOException
     */
    private void broadCastMessageToClient(SocketChannel sourceSocketChannel,
                                          Selector selector, String request) throws IOException {
        // 1. 获取所有的SelectionKey
        Set<SelectionKey> selectionKeys = selector.keys();

        Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
        while (selectionKeyIterator.hasNext()) {
            Channel targetChannel = selectionKeyIterator.next().channel();
            // 如果是SocketChannel，并且不是当前发送请求的SocketChannel，才发送数据
            if (targetChannel instanceof SocketChannel && targetChannel != sourceSocketChannel) {
                ((SocketChannel) targetChannel).write(Charset.forName("UTF-8").encode(request));
            }
        }
    }
}
