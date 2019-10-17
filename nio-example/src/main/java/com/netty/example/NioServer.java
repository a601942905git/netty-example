package com.netty.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * com.netty.example.NioServer
 * Nio服务器端实现
 *
 * @author lipeng
 * @date 2019/10/16 下午7:23
 */
public class NioServer {

    public static void main(String[] args) throws IOException {
        new NioServer().start();
    }

    public void start() throws IOException {
        // 1、创建多路复用器Selector
        Selector selector = Selector.open();

        // 2、根据ServerSocketChannel创建Channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 3、给Channel绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(8000));

        // 4、将Channel设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 5、将Channel注册到Selector上，并注册连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器已启动......");

        // 6、循环多路复用器
        for (; ; ) {
            // 阻塞等待，直到有Channel已就绪，返回对应的key的数量
            int readyCount = selector.select();

            // 就绪Channel数量为0，因为上面的方法可能返回0
            if (readyCount == 0) {
                continue;
            }

            // 获取selectionKeys集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 将selectionKey从集合中移除
                iterator.remove();

                // 7、根据事件类型调用对应的业务处理方法
                // 如果是连接事件
                if (selectionKey.isAcceptable()) {
                    acceptHandler(serverSocketChannel, selector);
                }

                // 如果是可读事件
                if (selectionKey.isReadable()) {
                    readHandler(selectionKey, selector);
                }
            }
        }
    }

    /**
     * 连接事件处理器
     *
     * @param serverSocketChannel serverSocketChannel
     * @param selector 多路复用器
     */
    private void acceptHandler(ServerSocketChannel serverSocketChannel, Selector selector)
            throws IOException {

        // 根据ServerSocketChannel创建SocketChannel
        // 类似于BIO中创建Socket一样
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 设置SocketChannel为非阻塞模式
        socketChannel.configureBlocking(false);

        // 将SocketChannel注册到多路复用器上，监听可读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 向客户端回复消息
        socketChannel.write(Charset.forName("UTF-8")
                .encode("您与聊天室中其它人不是好友关系，请注意隐私问题！"));
    }

    /**
     * 可读事件处理器
     *
     * @param selectionKey selectionKey
     * @param selector 多路复用器
     * @throws IOException
     */
    private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        // 根据SelectionKey获取就绪的Channel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        socketChannel.configureBlocking(false);
        // 创建一个长度为1024的字节缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        String request = "";
        // 循环读取客户端请求的数据
        // 将客户端请求的数据写入到字节缓冲区中
        while (socketChannel.read(byteBuffer) > 0) {
            // 读取缓冲区中的内容，需要将Buffer切换成读模式
            byteBuffer.flip();
            request += Charset.forName("UTF-8").decode(byteBuffer);
        }

        // 将SocketChannel注册到多路复用器上，继续监听可读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 将客户端请求的内容广播给其它客户端
        System.out.println("客户端内容：" + request);
        broadCast(selector, socketChannel, request);
    }

    /**
     * 广播消息
     *
     * @param selector 多路复用器
     * @param sourceChannel 源Channel
     * @param request 源Channel请求的内容
     * @throws IOException
     */
    private void broadCast(
            Selector selector, SocketChannel sourceChannel, String request) throws IOException {

        // 获取所有注册到Selector上的Channel
        Set<SelectionKey> selectionKeys = selector.keys();

        // 遍历所有的channel，然后进行消息发送
        for (SelectionKey key : selectionKeys) {
            Channel targetChannel = key.channel();
            // 目标Channel和sourceChannel不一样才进行信息发送
            if (targetChannel instanceof SocketChannel &&
                    targetChannel != sourceChannel) {
                ((SocketChannel) targetChannel).write(Charset.forName("UTF-8").encode(request));
            }
        }
    }
}
