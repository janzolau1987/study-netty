package com.yohotao.study.netty.demo01;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端，包括以下几步：
 * 》连接服务器
 * 》写数据到服务器
 * 》等待接受服务器返回相同的数据
 * 》关闭连接
 * 
 * @author liujianzhu
 * @date 2016年8月18日 下午9:36:57
 */
public class EchoClient {
	private final String host;
	private final int port;
	
	public EchoClient(String host,int port) {
		this.host = host;
		this.port = port;
	}
	
	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
				.channel(NioSocketChannel.class)
				.remoteAddress(new InetSocketAddress(host, port))
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new EchoClientHandler());
					}
				});
			ChannelFuture f = b.connect().sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new EchoClient("127.0.0.1", 8007).start();
	}
}
