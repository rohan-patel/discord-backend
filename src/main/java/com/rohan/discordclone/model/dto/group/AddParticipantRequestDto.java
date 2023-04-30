package com.rohan.discordclone.model.dto.group;

import lombok.Data;

@Data
public class AddParticipantRequestDto {

	private String groupId;
	private String participant;

	public AddParticipantRequestDto(String groupId, String participant) {
		super();
		this.groupId = groupId;
		this.participant = participant;
	}

	public AddParticipantRequestDto() {
		super();
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

}
