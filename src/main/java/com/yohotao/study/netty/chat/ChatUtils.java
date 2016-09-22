package com.yohotao.study.netty.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.Channel;

/**
 * 工具类
 * 
 * @author liujianzhu
 * @date 2016年9月22日 下午3:56:39
 */
public class ChatUtils {
	private final static String CHAT_ROOT_PATH = "chat/"; //文件存放根路径
	private final static String FILE_SUFFIX = ".html";
	
	/**
	 * 根据请求uri获取对应chat中目标文件路径
	 * @param uri
	 * @return
	 */
	public static String getPathByUri(String uri) {
		if(uri == null 
				|| "/".equals(uri)
				|| uri.trim().length() == 0)
			uri =	"login";
		String targetPath = CHAT_ROOT_PATH + uri;
		if(uri.indexOf(".") < 0) {
			targetPath +=  FILE_SUFFIX;
		}
		targetPath = ChatUtils.class.getClassLoader().getResource(targetPath).getPath();
		return targetPath;
	}
	
	/**
	 * 判断路径文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean checkFileExists(String uri) {
		File file = new File(getPathByUri(uri));
		if(file.exists() && file.isFile()) {
			return true;
		}
		return false;
	}
	
	//
	private static final Map<String, Channel> CHANNEL_MAP = new HashMap<>();
	private static final AtomicLong USER_INDEX = new AtomicLong(1);
	
	public static String addChannel(Channel channel) {
		String user = "用户" + USER_INDEX.getAndIncrement();
		addUserChannel(user, channel);
		return user;
	}
	
	public static List<String> getUsers() {
		Set<String> userSet = CHANNEL_MAP.keySet();
		return new ArrayList<>(userSet);
	}
	
	public static String removeChannel(Channel ch) {
		Iterator<String> iter = CHANNEL_MAP.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			Channel channel = CHANNEL_MAP.get(key);
			if(channel == ch){
				CHANNEL_MAP.remove(key);
				return key;
			}
		}
		return null;
	}
	
	public static void addUserChannel(String user,Channel channel) {
		CHANNEL_MAP.put(user, channel);
	}
	
	public static Channel getChannelByUser(String user) {
		return CHANNEL_MAP.get(user);
	}
}
