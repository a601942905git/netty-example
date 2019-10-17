package com.netty.example.chatroom;

import com.netty.example.NioClient1;

import java.io.IOException;

/**
 * com.netty.example.chatroom.Aclietn
 * 张三客户端，用来模拟聊天室中的一个用户
 *
 * @author lipeng
 * @date 2019/10/17 下午7:19
 */
public class ZhangsanClient {

    public static void main(String[] args) throws IOException {
        new NioClient1().start("张三");
    }
}
