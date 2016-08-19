package com.yohotao.study.netty.demo01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 服务器，主要由两部分组成：
 * 》配置服务器功能，如线程、端口
 * 》实现服务器处理程序，它包括业务逻辑，决定当有一个请求连接或接收数据时该做什么
 * 
 * @author liujianzhu
 * @date 2016年8月18日 下午9:35:37
 */
public class EchoServer {
	private final int port;
	
	public EchoServer(int port) {
		this.port = port;
	}
	
	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			//create ServerBootstrap instance
			ServerBootstrap b = new ServerBootstrap();
			//Specifies NIO transport, local socket address
			//Adds handler to channel pipeline
			b.group(group)
				.channel(NioServerSocketChannel.class)
				.localAddress(port)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new EchoServerHandler());
					}
				});
			//Binds server, waits for server to close , and releases resources
			ChannelFuture f = b.bind().sync();
			System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());;
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new EchoServer(8007).start();
	}
}
