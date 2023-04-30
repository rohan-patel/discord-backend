package com.rohan.discordclone.socket.model.dto;

import java.util.List;

import com.rohan.discordclone.socket.model.message.Message;

import lombok.Data;

@Data
public class ChatUpdateEventSendDto {

	private List<Message> messages;
	private List<String> participants;

	public ChatUpdateEventSendDto(List<Message> messages, List<String> participants) {
		super();
		this.messages = messages;
		this.participants = participants;
	}

	public ChatUpdateEventSendDto() {
		super();
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<String> getParticipants() {
		return participants;
	}

	public void setParticipants(List<String> participants) {
		this.participants = participants;
	}

}
