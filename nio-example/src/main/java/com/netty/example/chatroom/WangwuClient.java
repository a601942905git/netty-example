package com.netty.example.chatroom;

import com.netty.example.NioClient1;

import java.io.IOException;

/**
 * com.netty.example.chatroom.WangwuClient
 * 王五客户端，用来模拟聊天室中的一个用户
 *
 * @author lipeng
 * @date 2019/10/17 下午7:20
 */
public class WangwuClient {

    public static void main(String[] args) throws IOException {
        new NioClient1().start("王五");
    }
}
