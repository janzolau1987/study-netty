package com.yohotao.study.netty.demo07;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

public class SimpleChatServer {
	private int port;
	
	private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	public SimpleChatServer(int port) {
		this.port = port;
	}
	
	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new SimpleChatServerInitializer(channels))
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			System.out.println("SimpleChatServer启动");
			
			//绑定端口，开始接收进来的连接
			ChannelFuture f = b.bind(port).sync();
			
			//等待服务器socket关闭
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			System.out.println("SimpleChatServer关闭");
		}
	}
	
	public static void main(String[] args) throws Exception{
		int port;
		if(args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8888;
		}
		new SimpleChatServer(port).run();
	}
}
