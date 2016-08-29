package com.yohotao.study.netty.demo06;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 使用pojo对象传输
 * 
 * @author liujianzhu
 * @date 2016年8月29日 上午10:09:48
 */
public class TimeClient {
	public static void main(String[] args) throws Exception{
		String host = "localhost";
		int port = 8799;
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(bossGroup).channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new TimeDecoder());
						ch.pipeline().addLast(new TimeClientHandler());
					}
				});
			ChannelFuture f = b.connect(host, port).sync();
			//
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
		}
	}
}
