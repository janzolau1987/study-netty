package com.yohotao.study.netty.demo00;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class HelloClient {
	private static String host = "127.0.0.1";
	private static int port = 7878;

	public static void main(String[] args) throws InterruptedException, IOException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new HelloClientInitializer());
			// 连接服务端
			Channel ch = b.connect(host, port).sync().channel();
			// 控制台输入
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			for (;;) {
				String line = in.readLine();
				if (line == null)
					continue;
				ch.writeAndFlush(line + "\r\n");
			}
		} finally {
			group.shutdownGracefully();
		}
	}
}
