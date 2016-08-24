package com.yohotao.study.netty.demo03;

import java.net.InetSocketAddress;

import javax.net.ssl.SSLContext;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;

/**
 * 访问地址： https://localhost:4096
 * 
 * @author liujianzhu
 * @date 2016年8月24日 上午9:31:29
 */
public class SecureChatServer extends ChatServer {
	private final SSLContext context;
	
	public SecureChatServer(SSLContext context) {
		this.context = context;
	}
	
	@Override
	protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {
		return new SecureChatServerInitializer(group, context);
	}
	
	/**
	 * 获取SSLContext需要相关的kestore文件
	 * @return
	 */
	private static SSLContext getSslContext() {
		return null;
	}
	
	public static void main(String[] args) {
		SSLContext context = getSslContext();
		final SecureChatServer server = new SecureChatServer(context);
		ChannelFuture future = server.start(new InetSocketAddress(4096));
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				server.destory();
			}
		});
		future.channel().closeFuture().syncUninterruptibly();
	}
}
