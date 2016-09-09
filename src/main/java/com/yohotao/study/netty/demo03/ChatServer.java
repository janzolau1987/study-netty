package com.yohotao.study.netty.demo03;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * 访问地址 http://localhost:2048
 * 
 * @author liujianzhu
 * @date 2016年8月23日 下午1:58:14
 */
public class ChatServer {
	private final ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	private Channel channel;
	
	public ChannelFuture start(InetSocketAddress address) {
		ServerBootstrap b = new ServerBootstrap();
		b.group(workerGroup).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childHandler(createInitializer(group));
		ChannelFuture f = b.bind(address).syncUninterruptibly();
		channel = f.channel();
		return f;
	}
	
	public void destory() {
		if(channel != null)
			channel.close();
		group.close();
		workerGroup.shutdownGracefully();
	}
	
	protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {
		return new ChatServerInitializer(group);
	}
	
	public static void main(String[] args) throws Exception{
		final ChatServer server = new ChatServer();
		ChannelFuture f = server.start(new InetSocketAddress(7048));
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				server.destory();
			}
		});
		f.channel().closeFuture().syncUninterruptibly();
	}
}
