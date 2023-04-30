package com.rohan.discordclone.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "invitations")
public class FriendInvitation {

	@Id
	@PrimaryKeyColumn(name = "invitation_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String invitationId;

	@Column("sender_id")
	@CassandraType(type = Name.TEXT)
	private String senderId;

	@Column("receiver_id")
	@CassandraType(type = Name.TEXT)
	private String receiverId;

	public FriendInvitation(String invitationId, String senderId, String receiverId) {
		super();
		this.invitationId = invitationId;
		this.senderId = senderId;
		this.receiverId = receiverId;
	}

	public FriendInvitation() {
		super();
	}

	public String getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

}
