package com.rohan.discordclone.socket.model.dto;

import lombok.Data;

@Data
public class GroupChaUpdateEventReceiveDto {

	private String groupId;

	public GroupChaUpdateEventReceiveDto(String groupId) {
		super();
		this.groupId = groupId;
	}

	public GroupChaUpdateEventReceiveDto() {
		super();
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
