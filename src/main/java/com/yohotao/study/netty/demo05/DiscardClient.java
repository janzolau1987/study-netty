package com.yohotao.study.netty.demo05;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * 测试客户端
 * 
 * @author liujianzhu
 * @date 2016年8月26日 下午4:19:26
 */
public class DiscardClient {
	private String host;
	private int port;
	
	public DiscardClient(String host,int port) {
		this.host = host;
		this.port = port;
	}
	
	public void run() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
				.remoteAddress(new InetSocketAddress(host, port))
				.handler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
							@Override
							public void channelActive(ChannelHandlerContext ctx) throws Exception {
								ctx.writeAndFlush(Unpooled.copiedBuffer("Hello DiscardServer.", CharsetUtil.UTF_8));
							}
						});
					}
				})
				.option(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.connect().sync();
			//
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception{
		String host = "127.0.0.1";
		int port = 8880;
		new DiscardClient(host,port).run();
	}
}
