package com.rohan.discordclone.socket.model.dto;

import lombok.Data;

@Data
public class InviteDecisionsDto {

	private String id;

	public InviteDecisionsDto(String id) {
		super();
		this.id = id;
	}

	public InviteDecisionsDto() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
