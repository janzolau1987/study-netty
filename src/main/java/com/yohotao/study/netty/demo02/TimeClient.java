package com.yohotao.study.netty.demo02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
	public static void main(String[] args) throws Exception{
		String host = "localhost";
		int port = 8080;
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(workerGroup)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new TimeClientHandler());
					}
				})
				.option(ChannelOption.SO_KEEPALIVE, true);
			//start the client
			ChannelFuture f = b.connect(host,port).sync();
			
			//wait until the connection is closed
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
}
