package com.rohan.discordclone.model.dto.friendinvitation;

import lombok.Data;

@Data
public class InviteFriendDto {
	
	private String principal;
	

	public InviteFriendDto() {
	}

	public InviteFriendDto(String principal) {
		super();
		this.principal = principal;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
}
