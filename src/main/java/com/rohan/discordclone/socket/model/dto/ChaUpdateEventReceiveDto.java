package com.rohan.discordclone.socket.model.dto;

import lombok.Data;

@Data
public class ChaUpdateEventReceiveDto {

	private String receiverUserId;

	public ChaUpdateEventReceiveDto(String receiverUserId) {
		super();
		this.receiverUserId = receiverUserId;
	}

	public ChaUpdateEventReceiveDto() {
		super();
	}

	public String getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(String receiverUserId) {
		this.receiverUserId = receiverUserId;
	}

}
