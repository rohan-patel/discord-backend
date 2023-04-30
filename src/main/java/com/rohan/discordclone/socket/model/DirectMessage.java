package com.rohan.discordclone.socket.model;

import lombok.Data;

@Data
public class DirectMessage {

	private String receiverId;
	private String senderUsername;
	private String content;

	public DirectMessage(String receiverId, String senderUsername, String content) {
		super();
		this.receiverId = receiverId;
		this.senderUsername = senderUsername;
		this.content = content;
	}

	public DirectMessage() {
		super();
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
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
