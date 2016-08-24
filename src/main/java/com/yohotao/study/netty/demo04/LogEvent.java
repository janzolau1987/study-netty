package com.yohotao.study.netty.demo04;

import java.net.InetSocketAddress;

/**
 * 日志事件POJO
 * 
 * @author liujianzhu
 * @date 2016年8月24日 上午10:04:49
 */
public class LogEvent {
	public static final byte SEPARATOR = (byte) '|';

	private final InetSocketAddress source;
	private final String logfile;
	private final String msg;
	private final long received;

	public LogEvent(String logfile, String msg) {
		this(null, -1, logfile, msg);
	}

	public LogEvent(InetSocketAddress source, long received, String logfile, String msg) {
		this.source = source;
		this.received = received;
		this.logfile = logfile;
		this.msg = msg;
	}

	public InetSocketAddress getSource() {
		return source;
	}

	public String getLogfile() {
		return logfile;
	}

	public String getMsg() {
		return msg;
	}

	public long getReceived() {
		return received;
	}
}
