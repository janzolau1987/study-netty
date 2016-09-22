package com.yohotao.study.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * WebSocket,处理消息
 * 
 * @author liujianzhu
 * @date 2016年8月23日 下午12:07:53
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	private final ChannelGroup group;

	public TextWebSocketFrameHandler(ChannelGroup group) {
		this.group = group;
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 如果WebSocket握手完成
		if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
			// 删除ChannelPipeline中的HttpRequestHttpHandler
			ctx.pipeline().remove(HttpRequestHandler.class);
			String user = ChatUtils.addChannel(ctx.channel());
			Users us = new Users(user);
			ctx.channel().writeAndFlush(new TextWebSocketFrame(us.getCurrentUser()));
			// 写一个消息到ChannelGroup
			group.writeAndFlush(new TextWebSocketFrame(user + " 加入聊天室."));
			// 将channel添加到ChannelGroup
			group.add(ctx.channel());
			group.writeAndFlush(new TextWebSocketFrame(us.getAllUsers()));
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		ChatObject chatObj = new ChatObject(msg.text());
		if ("all".equals(chatObj.getToUser())) {
			// 将接收的消息通过ChannelGroup转发到所有已连接的客户端
			final Channel ch = ChatUtils.getChannelByUser(chatObj.getFromUser());
			ch.writeAndFlush(new TextWebSocketFrame("我对所有人说: " + chatObj.getMessage()));
			//
			group.writeAndFlush(new TextWebSocketFrame(chatObj.getFromUser()+"对所有人说: " + chatObj.getMessage()), 
					new ChannelMatcher() {
						@Override
						public boolean matches(Channel channel) {
							if(channel != ch && channel.isActive()) {
								return true;
							}
							return false;
						}
			});
		} else {
			Channel ch = ChatUtils.getChannelByUser(chatObj.getToUser());
			if(ch.isActive()) {
				ctx.writeAndFlush(new TextWebSocketFrame("我对" + chatObj.getToUser() + "说: " + chatObj.getMessage()));
				ch.writeAndFlush(new TextWebSocketFrame(chatObj.getFromUser() + "对我说: " + chatObj.getMessage()));
			}
			else {
				ChatUtils.removeChannel(ch);
				ctx.writeAndFlush(new TextWebSocketFrame("发送消息失败，原因："+chatObj.getToUser() + "已退出聊天室."));
				group.remove(ch);
				group.writeAndFlush(new TextWebSocketFrame(new Users(chatObj.getFromUser()).getAllUsers()));
			}
		}
	}
}
