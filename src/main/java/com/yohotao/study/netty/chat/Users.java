package com.yohotao.study.netty.chat;

import java.util.List;

public class Users {
	private String currentUser;
	private List<String> users;
	
	public Users(String currentUser) {
		this.currentUser = currentUser;
		this.users = ChatUtils.getUsers();
	}
	
	public String getAllUsers() {
		String ustr = null;
		for(String u : users) {
			if(ustr == null)
				ustr = "\""+ u + "\"";
			else
				ustr += ",\""+ u + "\"";
				
		}
		return "Users={\"users\":[" + ustr + "]}";
	}
	
	public String getCurrentUser() {
		return "CurrentUser="+this.currentUser;
	}
}
