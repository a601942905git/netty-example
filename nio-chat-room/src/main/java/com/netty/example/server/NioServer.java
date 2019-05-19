package com.netty.example.server;

import java.io.IOException;

/**
 * com.netty.example.NioServer
 *
 * @author lipeng
 * @date 2019-05-19 12:56
 */
public class NioServer {

    public static void main(String[] args) throws IOException {
        NioServerProcessor nioServerProcessor = new NioServerProcessor();
        nioServerProcessor.start();
    }
}
