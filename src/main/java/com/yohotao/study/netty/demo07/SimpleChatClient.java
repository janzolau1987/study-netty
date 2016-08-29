package com.yohotao.study.netty.demo07;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SimpleChatClient {
	private final String host;
	private final int port;
	
	public SimpleChatClient(String host,int port) {
		this.host = host;
		this.port = port;
	}
	
	public void run() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
				.channel(NioSocketChannel.class)
				.handler(new SimpleChatClientInitializer());
			Channel channel = b.connect(host, port).sync().channel();
			
			//
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while(true) {
				channel.writeAndFlush(in.readLine() + "\r\n");
			}
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new SimpleChatClient("localhost", 8888).run();
	}
}
