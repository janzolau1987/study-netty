package com.yohotao.study.netty.demo04;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

/**
 * 自定义广播器
 * 
 * @author liujianzhu
 * @date 2016年8月24日 上午10:10:11
 */
public class LogEventBroadcaster {
	private final EventLoopGroup group;
	private final Bootstrap bootstrap;
	private final File file;

	public LogEventBroadcaster(InetSocketAddress address, File file) {
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
				.handler(new LogEventEncoder(address));
		this.file = file;
	}

	public void run() throws IOException {
		Channel ch = bootstrap.bind(0).syncUninterruptibly().channel();
		long pointer = 0;
		for (;;) {
			long len = file.length();
			if (len < pointer) {
				pointer = len;
			} else {
				RandomAccessFile raf = new RandomAccessFile(file, "r");
				raf.seek(pointer);
				String line;
				while ((line = raf.readLine()) != null) {
					line = new String(line.getBytes(CharsetUtil.ISO_8859_1),CharsetUtil.UTF_8);
					ch.write(new LogEvent(null, -1, file.getAbsolutePath(), line));
				}
				ch.flush();
				pointer = raf.getFilePointer();
				raf.close();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.interrupted();
				break;
			}
		}
	}

	public void stop() {
		group.shutdownGracefully();
	}

	public static void main(String[] args) throws Exception {
		int port = 4096;
		String path = LogEventMonitor.class.getResource("/").getPath() + "/surface.log";
		LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255", port),
				new File(path));
		try {
			broadcaster.run();
		} finally {
			broadcaster.stop();
		}
	}
}
