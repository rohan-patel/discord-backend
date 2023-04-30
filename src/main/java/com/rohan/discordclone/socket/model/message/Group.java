package com.rohan.discordclone.socket.model.message;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "groups")
public class Group {

	@Id
	@PrimaryKeyColumn(name = "group_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String groupId;

	@Column("group_name")
	@CassandraType(type = Name.TEXT)
	private String groupName;

	@Column("conversation_id")
	@CassandraType(type = Name.TEXT)
	private String conversationId;

	public Group(String groupId, String groupName, String conversationId) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.conversationId = conversationId;
	}

	public Group() {
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

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

}
