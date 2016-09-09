package com.yohotao.study.netty.demo09;

import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;

/**
 * 测试异常处理
 * 
 * @author liujianzhu
 * @date 2016年9月9日 下午4:19:27
 */
public class FrameChunkDecoderTest {
	@Test
	public void testFrameDecoded() {
		ByteBuf buf = Unpooled.buffer();
		for(int i=0;i<9;i++) {
			buf.writeByte(i);
		}
		ByteBuf input = buf.duplicate();
		
		EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));
		Assert.assertTrue(channel.writeInbound(input.readBytes(2)));
		try{
			channel.writeInbound(input.readBytes(4));
			Assert.fail();
		} catch(TooLongFrameException e) {
			System.out.println("异常： " + e.getMessage());
		}
		Assert.assertTrue(channel.writeInbound(input.readBytes(3)));
		
		Assert.assertTrue(channel.finish());
		
		ByteBuf read = (ByteBuf) channel.readInbound();
		Assert.assertEquals(buf.readSlice(2), read);
		read.release();
		
		read = (ByteBuf) channel.readInbound();
		Assert.assertEquals(buf.skipBytes(4).readBytes(3),read);
		read.release();
		
		buf.release();
	}
}
