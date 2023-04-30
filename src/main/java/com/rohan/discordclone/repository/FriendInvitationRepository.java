package com.rohan.discordclone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import com.rohan.discordclone.model.FriendInvitation;

public interface FriendInvitationRepository extends CassandraRepository<FriendInvitation, String> {
	
	@AllowFiltering
	Optional<FriendInvitation> findBySenderId(String senderId);
	
	@AllowFiltering
	Optional<FriendInvitation> findByReceiverId(String receiverId);
	
	@AllowFiltering
	Optional<List<FriendInvitation>> findAllByReceiverId(String receiverId);
	
	@AllowFiltering
	boolean existsBySenderIdAndReceiverId(String senderId, String receiverId);
	
	@AllowFiltering
	Optional<FriendInvitation> findBySenderIdAndReceiverId(String senderId, String receiverId);
	
	@AllowFiltering
	void deleteBySenderIdAndReceiverId(String senderId, String receiverId);
	
}
