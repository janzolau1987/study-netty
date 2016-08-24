package com.yohotao.study.netty.demo04;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * 客户端接收程序
 * 
 * @author liujianzhu
 * @date 2016年8月24日 上午10:51:27
 */
public class LogEventMonitor {
	private final EventLoopGroup group;
	private final Bootstrap bootstrap;

	public LogEventMonitor(InetSocketAddress address) {
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
				.handler(new ChannelInitializer<Channel>() {
					protected void initChannel(Channel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new LogEventDecoder());
						pipeline.addLast(new LogEventHandler());
					}
				}).localAddress(address);
	}

	public Channel bind() {
		return bootstrap.bind().syncUninterruptibly().channel();
	}

	public void stop() {
		group.shutdownGracefully();
	}

	public static void main(String[] args) throws InterruptedException {
		LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(4096));
		try {
			Channel channel = monitor.bind();
			System.out.println("LogEventMonitor running");
			channel.closeFuture().sync();
		} finally {
			monitor.stop();
		}
	}
}
