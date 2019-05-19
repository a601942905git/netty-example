package com.netty.example.client;

import java.io.IOException;

/**
 * com.netty.example.client.NioClientC
 *
 * @author lipeng
 * @date 2019-05-19 21:11
 */
public class NioClientC {
    public static void main(String[] args) throws IOException {
        NioClientProcessor nioClientProcessor = new NioClientProcessor("CCC");
        nioClientProcessor.start();
    }
}
