package com.yohotao.study.netty.demo03;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslHandler;

/**
 * WebSocket,加密
 * 
 * @author liujianzhu
 * @date 2016年8月24日 上午9:28:25
 */
public class SecureChatServerInitializer extends ChatServerInitializer {
	private final SSLContext context;

	public SecureChatServerInitializer(ChannelGroup group, SSLContext context) {
		super(group);
		this.context = context;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		super.initChannel(ch);
		//
		SSLEngine engine = context.createSSLEngine();
		engine.setUseClientMode(false);
		ch.pipeline().addLast(new SslHandler(engine));
	}
}
