package com.yohotao.study.netty.demo09;

import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * 测试ChannelHandler
 * 
 * @author liujianzhu
 * @date 2016年9月9日 下午4:04:57
 */
public class FixedLengthFrameDecoderTest {
	@Test
	public void testFrameDecoded() {
		ByteBuf buf = Unpooled.buffer();
		for (int i = 0; i < 9; i++) {
			buf.writeByte(i);
		}
		ByteBuf input = buf.duplicate();
		EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
		Assert.assertFalse(channel.writeInbound(input.readBytes(2)));
		Assert.assertTrue(channel.writeInbound(input.readBytes(7)));
		
		Assert.assertTrue(channel.finish());
		ByteBuf read = (ByteBuf) channel.readInbound();
		Assert.assertEquals(buf.readSlice(3), read);
		read.release();
		
		read = (ByteBuf) channel.readInbound();
		Assert.assertEquals(buf.readSlice(3), read);
		read.release();
		
		read = (ByteBuf) channel.readInbound();
		Assert.assertEquals(buf.readSlice(3), read);
		read.release();
		
		Assert.assertNull(channel.readInbound());
		buf.release();
	}
}
