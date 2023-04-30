package com.rohan.discordclone.model.dto.group;

import lombok.Data;

@Data
public class CreateGroupResponseDto {

	private String groupId;
	private String groupName;

	public CreateGroupResponseDto(String groupId, String groupName) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
	}

	public CreateGroupResponseDto() {
		super();
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
