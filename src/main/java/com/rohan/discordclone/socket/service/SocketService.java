package com.rohan.discordclone.socket.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.rohan.discordclone.model.FriendInvitation;
import com.rohan.discordclone.model.UserDetails;
import com.rohan.discordclone.repository.FriendInvitationRepository;
import com.rohan.discordclone.repository.UserDetailsRepository;
import com.rohan.discordclone.socket.model.ConnectedUsers;

@Service
public class SocketService {
	
	@Autowired
	FriendInvitationRepository invitationRepo;
	
	@Autowired
	UserDetailsRepository userDetailsRepo;
	
//	public void sendMessage(String room, String eventName, SocketIOClient senderClient, String message) {
//		
//		for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
//			
//			if (!client.getSessionId().equals(senderClient.getSessionId())) {
//				client.sendEvent(eventName, new Message(MessageType.SERVER, message));
//			}
//		}
//	}
	
//	---------------------------------------------------------------------------------------------------------------------------------------
	
	/*	
	 * Updating Pending Invitation List 
	 */
	public void sendUpdatePendingInvitationsEvent(String userId, List<UserDetails> pendingInvitationUsers) {
		List<SocketIOClient> activeConnections = getActiveConnections(userId);
		for (SocketIOClient client : activeConnections) {
			client.sendEvent("friends-invitation", pendingInvitationUsers);
		}
	}
	
	public void sendPendingInvitationsEventByUserId(String userId) {
//		List containing Username and userId of the invitation sender
		List<UserDetails> pendingInvitationUserDetails = new ArrayList<UserDetails>();
		
//		Getting list of all the pending invitations
		Optional<List<FriendInvitation>> pendingInvitationsForTargetUsers = invitationRepo
				.findAllByReceiverId(userId);
		
//		Iterating through all pending invitations and getting details of the sender and adding to
//		the above list
		if (pendingInvitationsForTargetUsers.isPresent()) {
			for (FriendInvitation invitation : pendingInvitationsForTargetUsers.get()) {
				UserDetails invitationSender = userDetailsRepo.findById(invitation.getSenderId()).get();
				pendingInvitationUserDetails
				.add(new UserDetails(invitationSender.getUserId(), invitationSender.getUsername(), invitationSender.getEmail()));
			}
		}
		
//		Sending the event to the receiver including the UserDetails of the pending invitations 
		sendUpdatePendingInvitationsEvent(userId, pendingInvitationUserDetails);
		
	}
	
//	---------------------------------------------------------------------------------------------------------------------------------------
	
	/*	
	 * Updating Friends List 
	 */
	public void sendUpdateFriendsEvent(String userId, List<UserDetails> friends) {
		List<SocketIOClient> activeConnections = getActiveConnections(userId);
		for (SocketIOClient client : activeConnections) {
			client.sendEvent("friends-list", friends);
		}
	}
	
	
	public void sendFriendsEventByUserId(String userId) {
		Optional<UserDetails> userDetails = userDetailsRepo.findById(userId);
		if (userDetails.isPresent()) {
			List<String> friends = userDetails.get().getFriends();
			
			List<UserDetails> friendsDetails = new ArrayList<>();
			if (friends != null) {
				friendsDetails = userDetailsRepo.findAllById(friends);
			}
			for (UserDetails user : friendsDetails) {
				user.setFriends(null);
			}
			sendUpdateFriendsEvent(userId, friendsDetails);
		}
	}
	
//	---------------------------------------------------------------------------------------------------------------------------------------
	
	/*	
	 * Updating Online Users 
	 */
	
	@Scheduled(fixedDelay = 30000)
	public void sendUpdateOnlieUsersEvent() {
		
//		List<String> onlineUsers = getOnlineUsers();
		Map<SocketIOClient,String> connectedUsers = ConnectedUsers.getConnectedUsers();
		for (Map.Entry<SocketIOClient, String> entry : connectedUsers.entrySet()) {
			SocketIOClient client = entry.getKey();
			client.sendEvent("online-users", getOnlineUsers());
		}
		
	}
	
	public Set<String> getOnlineUsers() {
		Map<SocketIOClient, String> connectedUsers = ConnectedUsers.getConnectedUsers();
		Set<String> onlineUsers = new HashSet<>();
		for (Map.Entry<SocketIOClient, String> entry : connectedUsers.entrySet()) {
			onlineUsers.add(entry.getValue());
		}
		return onlineUsers;
	}
	
//	---------------------------------------------------------------------------------------------------------------------------------------
	
	/*
	 * Utilities
	 */
	
	public List<SocketIOClient> getActiveConnections(String userId) {
		List<SocketIOClient> activeConnections = new ArrayList<SocketIOClient>();
		
		Map<SocketIOClient,String> connectedUsers = ConnectedUsers.getConnectedUsers();
		for (Map.Entry<SocketIOClient, String> entry : connectedUsers.entrySet()) {
			if(entry.getValue().equals(userId)) {
				activeConnections.add(entry.getKey());
			}
		}
		return activeConnections;
	}

}
