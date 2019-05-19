package com.netty.example.client;

import java.io.IOException;

/**
 * com.netty.example.client.NioClient
 *
 * @author lipeng
 * @date 2019-05-19 17:47
 */
public class NioClientA {

    public static void main(String[] args) throws IOException {
        NioClientProcessor nioClientProcessor = new NioClientProcessor("AAA");
        nioClientProcessor.start();
    }
}
