package com.rohan.discordclone.socket;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.rohan.discordclone.socket.model.ConnectedUsers;
import com.rohan.discordclone.socket.model.DirectMessage;
import com.rohan.discordclone.socket.model.GroupMessage;
import com.rohan.discordclone.socket.model.Principal;
import com.rohan.discordclone.socket.model.dto.ChaUpdateEventReceiveDto;
import com.rohan.discordclone.socket.model.dto.GroupChaUpdateEventReceiveDto;
import com.rohan.discordclone.socket.service.SocketGroupService;
import com.rohan.discordclone.socket.service.SocketMessagingService;
import com.rohan.discordclone.socket.service.SocketService;

@Component
public class SocketModule {

	private SocketIOServer server;
	
	@Autowired
	private SocketService socketService;
	
	@Autowired
	private SocketMessagingService messageService;
	
	@Autowired
	private SocketGroupService groupService;
	
	Logger logger = LoggerFactory.getLogger(SocketModule.class);

	public SocketModule(SocketIOServer server) {
		this.server = server;
		
		server.addConnectListener(onConnected());
		server.addDisconnectListener(onDisconnected());
		server.addEventListener("direct-message", DirectMessage.class, onDirectMessageReceived());
		server.addEventListener("group-message", GroupMessage.class, onGroupMessageReceived());
		server.addEventListener("direct-chat-history", ChaUpdateEventReceiveDto.class, onChatUpdateMessage());
		server.addEventListener("group-chat-history", GroupChaUpdateEventReceiveDto.class, onGroupChatUpdateMessage());
//		server.addEventListener("send_message", Message.class, onChatRecieved());
	}
	
	private DataListener<GroupMessage> onGroupMessageReceived() {
		// TODO Auto-generated method stub
		return (senderClient, data, ackSender) -> {
			String groupId = data.getGroupId();
			String senderId = ConnectedUsers.getUser(senderClient);
			String senderUsername = data.getSenderUsername();
			String content = data.getContent();
			
			System.out.println();
			System.out.println();
			System.out.println("Adding Group Conversation");
			System.out.println("Group ID: " + groupId);
			System.out.println("Sender ID: " + senderId);
			System.out.println("sender Username" + senderUsername);
			System.out.println("Content: " + content);
			System.out.println();
			System.out.println();
			
			groupService.addGroupConversation(senderId, senderUsername, content, groupId);
			
		};
	}

	private DataListener<GroupChaUpdateEventReceiveDto> onGroupChatUpdateMessage() {
		// TODO Auto-generated method stub
		return (senderClient, data, ackSender) -> {
			String groupId = data.getGroupId();
			System.out.println(groupId);
			String userId = ConnectedUsers.getUser(senderClient);
			groupService.sendGroupUpdateChatEventByUserId(userId, groupId);
			
		};
	}

	private DataListener<ChaUpdateEventReceiveDto> onChatUpdateMessage() {
		
		return (senderCLient, data, ackSender) -> {
			String receiverId = data.getReceiverUserId();
			String senderId = ConnectedUsers.getUser(senderCLient);
			
			String convoId = messageService.getConversationId(senderId, receiverId);
			
			messageService.sendUpdateChatEventByUserId(senderId, convoId);
		};
	}

	private DataListener<DirectMessage> onDirectMessageReceived() {
		return (senderClient, data, ackSender) -> {
//			System.out.println("Data: " + data.getContent());
//			System.out.println("Receiver Id: " + data.getReceiverId());
//			System.out.println("Sender Client ID: " + senderClient.getSessionId().toString());
			
			Map<SocketIOClient,String> connectedUsers = ConnectedUsers.getConnectedUsers();
			String senderId = connectedUsers.get(senderClient);
			String receiverId = data.getReceiverId();
			String senderUsername = data.getSenderUsername();
			String content = data.getContent();
			
			messageService.addConversation(senderId, senderUsername, content, receiverId);
		};
	}

//	private DataListener<Message> onChatRecieved() {
//		
//		return (senderClient, data, ackSender) -> {
//			logger.info(data.toString());
//			
//			socketService.sendMessage(data.getRoom(), "get_message", senderClient, data.getMessage());
//		};
//
//	}

	private ConnectListener onConnected() {
		return client -> {
			String socketId = client.getSessionId().toString();
			
			String principal = Principal.getPrincipal();
			System.out.println("Pricipal: "+principal);
			
			if (ConnectedUsers.addUser(client, principal)) {
				logger.info("Principal: {} added to the store", ConnectedUsers.getUser(client));
			}
			
			logger.info("Socket ID[{}] connected to socket", socketId);
			
			for (Map.Entry<SocketIOClient, String> entry : ConnectedUsers.getConnectedUsers().entrySet()) {
			    System.out.println(entry.getKey().getSessionId().toString() + ":" + entry.getValue().toString());
			}
			
			socketService.sendPendingInvitationsEventByUserId(principal);
			socketService.sendFriendsEventByUserId(principal);
			socketService.sendUpdateOnlieUsersEvent();
			groupService.sendGroupUpdateEventByUserId(principal);
			
		};
	}

	private DisconnectListener onDisconnected() {
		return client -> {
			String socketId = client.getSessionId().toString();
			
			String principal = ConnectedUsers.getUser(client);
			
			if (ConnectedUsers.removeUser(client)) {
				logger.info("Principal: {} removed from the store", principal);
			}
			
			logger.info("Socket ID[{}] disconnected from socket", socketId);
			
			for (Map.Entry<SocketIOClient, String> entry : ConnectedUsers.getConnectedUsers().entrySet()) {
				System.out.println(entry.getKey().getSessionId().toString() + ":" + entry.getValue().toString());
			}
		};
	}

	
	
}
