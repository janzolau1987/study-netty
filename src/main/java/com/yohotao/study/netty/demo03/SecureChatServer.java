package com.yohotao.study.netty.demo03;

import java.net.InetSocketAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * 访问地址： https://localhost:4096
 * 
 * @author liujianzhu
 * @date 2016年8月24日 上午9:31:29
 */
public class SecureChatServer extends ChatServer {
	private final SslContext context;

	public SecureChatServer(SslContext context) {
		this.context = context;
	}

	@Override
	protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {
		return new SecureChatServerInitializer(group, context);
	}

	/**
	 * 获取SSLContext需要相关的kestore文件
	 * 
	 * @return
	 * @throws CertificateException 
	 * @throws SSLException 
	 */
	private static SslContext getSslContext() throws Exception{
		SelfSignedCertificate cert = new SelfSignedCertificate();
		SslContext context = SslContextBuilder.forServer(cert.certificate(), cert.privateKey()).build();
		return context;
	}

	public static void main(String[] args) throws Exception{
		SslContext context = getSslContext();
		final SecureChatServer server = new SecureChatServer(context);
		ChannelFuture future = server.start(new InetSocketAddress(7048));
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				server.destory();
			}
		});
		future.channel().closeFuture().syncUninterruptibly();
	}
}
