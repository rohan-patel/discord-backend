package com.rohan.discordclone.socket.model.message;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "conversations")
public class Conversation {

	@Id
	@PrimaryKeyColumn(name = "conversation_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String conversationID;

//	UserID's of the participants
	@Column("participants")
	@CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
	private List<String> participants;

//	Message ID's of the messages in the conversation
	@Column("messages")
	@CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
	private List<String> messages;

	public Conversation(String conversationID, List<String> participants, List<String> messages) {
		super();
		this.conversationID = conversationID;
		this.participants = participants;
		this.messages = messages;
	}

	public Conversation(String conversationID, List<String> participants) {
		super();
		this.conversationID = conversationID;
		this.participants = participants;
	}

	public Conversation() {
		super();
	}

	public String getConversationID() {
		return conversationID;
	}

	public void setConversationID(String conversationID) {
		this.conversationID = conversationID;
	}

	public List<String> getParticipants() {
		return participants;
	}

	public void setParticipants(List<String> participants) {
		this.participants = participants;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

}
