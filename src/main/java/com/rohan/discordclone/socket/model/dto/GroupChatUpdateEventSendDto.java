package com.rohan.discordclone.socket.model.dto;

import java.util.List;

import com.rohan.discordclone.socket.model.message.Message;

import lombok.Data;

@Data
public class GroupChatUpdateEventSendDto {

	private String groupId;
	private List<Message> messages;

	public GroupChatUpdateEventSendDto(String groupId, List<Message> messages) {
		super();
		this.groupId = groupId;
		this.messages = messages;
	}

	public GroupChatUpdateEventSendDto(String groupId) {
		super();
		this.groupId = groupId;
	}

	public GroupChatUpdateEventSendDto() {
		super();
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}
