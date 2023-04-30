package com.rohan.discordclone.socket.model;

import lombok.Data;

@Data
public class GroupMessage {

	private String groupId;
	private String senderUsername;
	private String content;

	public GroupMessage(String groupId, String senderUsername, String content) {
		super();
		this.groupId = groupId;
		this.senderUsername = senderUsername;
		this.content = content;
	}

	public GroupMessage() {
		super();
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
