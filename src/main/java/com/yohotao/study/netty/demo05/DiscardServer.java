package com.yohotao.study.netty.demo05;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 丢弃任何进入的数据
 * 
 * @author liujianzhu
 * @date 2016年8月26日 下午3:56:48
 */
public class DiscardServer {
	private int port;
	
	public DiscardServer(int port) {
		this.port = port;
	}
	
	/**
	 * 代码解释：
	 * 1、NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器。“bossGroup”用来接收进来的连接，"workerGroup"用来
	 * 	处理已经被接收的连接，一旦'bossGroup'接收到连接，就会把连接信息注册到'workerGroup'上.
	 * 
	 * 2、ServerBootstrap是一个启动NIO服务的辅助启动类.
	 * 
	 * 3、NioServerSocketChannel 用来初始化新的Channel来接收连接（instantiate a new Channel to accept incoming connections）.
	 * 
	 * 4、 ChannelInitializer is a special handler that is purposed to help a user configure a new Channel.
	 * 
	 * 5、option() is for the NioServerSocketChannel that accepts incoming connections. 
	 * 	childOption() is for the Channels accepted by the parent ServerChannel, which is NioServerSocketChannel in this case.
	 * 
	 * 
	 */
	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new DiscardServerHandler());
					}
				})
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			//绑定端口，开始接收进来的连接
			ChannelFuture f = b.bind(port).sync();
			
			//等待服务器socket关闭
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception{
		int port ;
		if(args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8880;
		}
		new DiscardServer(port).run();
	}
}
