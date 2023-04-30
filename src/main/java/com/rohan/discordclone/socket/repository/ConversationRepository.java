package com.rohan.discordclone.socket.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import com.rohan.discordclone.socket.model.message.Conversation;

public interface ConversationRepository extends CassandraRepository<Conversation, String> {

	@AllowFiltering
	Optional<Conversation> findByParticipants(List<String> participants);
}
