package com.yohotao.study.netty.demo07;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {
	private final ChannelGroup channels;
	
	public SimpleChatServerHandler(ChannelGroup channels) {
		this.channels = channels;
	}
	
	/**
	 * 每当从服务端收到新客户端连接时，客户端的Channel存入ChannelGroup列表中，并通知列表中的其他客户端Channel
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		for(Channel channel : channels) {
			channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
		}
		channels.add(ctx.channel());
	}
	
	/**
	 * 每当从服务端收到客户端断开时，客户端的Channel移除ChannelGroup列表中，并通知列表中的其他客户端Channel
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		for(Channel channel : channels) {
			channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
		}
		channels.remove(ctx.channel());
	}

	/**
	 * 每当从服务端讲到客户端写入信息时，将信息转发给其他客户端的Channel
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel incoming = ctx.channel();
		for(Channel channel : channels) {
			if(channel != incoming) {
				channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + msg + "\n");
			} else {
				channel.writeAndFlush("[you]" + msg + "\n");
			}
		}
	}

	/**
	 * 服务端监听到客户端活动
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println("SimpleChatClient:" + incoming.remoteAddress()+"在线");
	}
	
	/**
	 * 服务端监听到客户端不活动
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println("SimpleChatClient:" + incoming.remoteAddress()+"掉线");
	}
	
	/**
	 * 当出现Throwable对象才会被调用，即当netty由于IO错误或者处理器在处理事件时抛出异常时。
	 * 在大部分情况下，捕获的异常应该被记录下来并且把关联的channel关闭掉。然而这个方法的处理方式会在遇到不同异常的情况下有不同的实现，
	 * 比如可能想在关闭连接之前发送一个错误码的响应消息。
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println("SimpleChatClient:" + incoming.remoteAddress()+"异常");
		//当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
}
