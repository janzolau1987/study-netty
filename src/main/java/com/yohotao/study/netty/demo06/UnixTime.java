package com.yohotao.study.netty.demo06;

import java.util.Date;

/**
 * POJO类
 * 
 * @author liujianzhu
 * @date 2016年8月29日 上午10:05:04
 */
public class UnixTime {
	private final long value;

	public UnixTime() {
		this(System.currentTimeMillis() / 1000L + 2208988800L);
	}

	public UnixTime(long value) {
		this.value = value;
	}

	public long value() {
		return value;
	}

	@Override
	public String toString() {
		return new Date((value() - 2208988800L) * 1000L).toString();
	}
}
