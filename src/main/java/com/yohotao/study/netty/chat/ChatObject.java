package com.yohotao.study.netty.chat;

import java.io.Serializable;

/**
 * 聊天信息对象
 * 
 * @author liujianzhu
 * @date 2016年9月22日 下午5:08:44
 */
public class ChatObject implements Serializable {
	private static final long serialVersionUID = 8780463440722905928L;
	private String fromUser = "";
	private String toUser = "";
	private String message = "";

	public ChatObject() {

	}

	public ChatObject(String msg) {
		String[] msgs = msg.split(":");
		if (msgs.length >= 3) {
			this.fromUser = msgs[0];
			this.toUser = msgs[1];
			this.message = msgs[2];
		}
	}

	public ChatObject(String fromUser, String toUser, String message) {
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.message = message;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
