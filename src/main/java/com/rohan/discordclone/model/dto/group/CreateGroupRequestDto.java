package com.rohan.discordclone.model.dto.group;

import lombok.Data;

@Data
public class CreateGroupRequestDto {

	private String groupName;
	private String userId;

	public CreateGroupRequestDto(String groupName, String userId) {
		super();
		this.groupName = groupName;
		this.userId = userId;
	}

	public CreateGroupRequestDto(String groupName) {
		super();
		this.groupName = groupName;
	}

	public CreateGroupRequestDto() {
		super();
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
