package com.yohotao.study.netty.demo00;

import java.net.InetAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * HelloWorld项目的自定义ChannelHandler
 * 
 * @author liujianzhu
 * @date 2016年8月24日 下午3:50:30
 */
public class HelloServerHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);
		ctx.writeAndFlush("Received your message!\n");
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("RemoteAddress : " + ctx.channel().remoteAddress() + " active!");
		ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");
		super.channelActive(ctx);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
