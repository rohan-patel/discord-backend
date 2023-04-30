package com.rohan.discordclone.socket.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.rohan.discordclone.model.UserDetails;
import com.rohan.discordclone.repository.UserDetailsRepository;
import com.rohan.discordclone.socket.model.ConnectedUsers;
import com.rohan.discordclone.socket.model.dto.GroupChatUpdateEventSendDto;
import com.rohan.discordclone.socket.model.message.Conversation;
import com.rohan.discordclone.socket.model.message.Group;
import com.rohan.discordclone.socket.model.message.Message;
import com.rohan.discordclone.socket.repository.ConversationRepository;
import com.rohan.discordclone.socket.repository.GroupRepository;
import com.rohan.discordclone.socket.repository.MessageRepository;

@Service
public class SocketGroupService {

	@Autowired
	private GroupRepository groupRepo;

	@Autowired
	private SocketMessagingService messageService;

	@Autowired
	private UserDetailsRepository userDetailsRepo;

	@Autowired
	private ConversationRepository convoRepo;

	@Autowired
	private MessageRepository messageRepo;

	public String createGroup(String groupName, String userId) {

		String groupId = UUID.randomUUID().toString();
		String convoId = UUID.randomUUID().toString();

//		Adding groupId in UserDetails
		addGroupIdInUserDetails(userId, groupId);

//		Creating group
		Group group = new Group(groupId, groupName, convoId);
		groupRepo.save(group);

//		Creating Conversation
		List<String> participants = new ArrayList<String>();
		participants.add(userId);
		Conversation convo = new Conversation(convoId, participants);
		convoRepo.save(convo);

		return groupId;
	}

	public void addParticipant(String groupId, String userId) {
		addGroupIdInUserDetails(userId, groupId);

		Group group = groupRepo.findById(groupId).get();
		String convoId = group.getConversationId();

		Conversation convo = convoRepo.findById(convoId).get();
		List<String> participants = convo.getParticipants();
		participants.add(userId);
		convoRepo.save(new Conversation(convo.getConversationID(), participants, convo.getMessages()));
		
		sendGroupUpdateEventByUserId(userId);
	}

	public void sendGroupUpdateEventByUserId(String userId) {
		UserDetails userDetails = userDetailsRepo.findById(userId).get();

		List<Group> groups = new ArrayList<>();
		List<String> groupIds = userDetails.getGroups();
		if (groupIds != null) {
			groups = groupRepo.findAllById(groupIds);
			System.out.println("Groups: "+groups);
		}
		List<Group> group = groups;

		Map<SocketIOClient, String> map = ConnectedUsers.getConnectedUsers();

		Map<SocketIOClient, String> userIdSocketClients = map.entrySet().stream()
				.filter(x -> Arrays.asList(userId).contains(x.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		userIdSocketClients.forEach((socketClient, user) -> {
//			System.out.println(socketClient.getSessionId().toString());
			socketClient.sendEvent("group-update-history", group);
		});

	}

	public void addGroupIdInUserDetails(String userId, String groupId) {
		UserDetails user = userDetailsRepo.findById(userId).get();
		List<String> groups = user.getGroups();
		if (groups == null) {
			List<String> newGroup = new ArrayList<String>();
			newGroup.add(groupId);
			groups = newGroup;
		} else {
			groups.add(groupId);
		}
		UserDetails userDetails = new UserDetails(user.getUserId(), user.getEmail(), user.getUsername(), groups,
				user.getFriends());
		userDetailsRepo.save(userDetails);
	}

	public void sendGroupUpdateChatEventByGroupId(String groupId) {
		Group group = groupRepo.findById(groupId).get();
		String convoId = group.getConversationId();
		Conversation convoObject = convoRepo.findById(convoId).get();

		List<String> participants = convoObject.getParticipants();

		for (String participant : participants) {
			sendGroupUpdateChatEventByUserId(participant, groupId);
		}
	}

	public void sendGroupUpdateChatEventByUserId(String userId, String groupId) {

		GroupChatUpdateEventSendDto eventSendDto = getGroupChaUpdateEventSendDto(groupId);

		Map<SocketIOClient, String> map = ConnectedUsers.getConnectedUsers();

		Map<SocketIOClient, String> userIdSocketClients = map.entrySet().stream()
				.filter(x -> Arrays.asList(userId).contains(x.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		userIdSocketClients.forEach((socketClient, user) -> {
			socketClient.sendEvent("group-chat-update-history", eventSendDto);
		});
	}

	public GroupChatUpdateEventSendDto getGroupChaUpdateEventSendDto(String groupId) {
		Group group = groupRepo.findById(groupId).get();
		String convoId = group.getConversationId();

		Conversation convoObject = convoRepo.findById(convoId).get();

		List<Message> messages = new ArrayList<Message>();

//		List<String> participants = convoObject.getParticipants();
		List<String> messageIds = convoObject.getMessages();
		if (messageIds != null) {
			List<Message> loadMessages = messageRepo.findAllById(messageIds);
			messages = loadMessages;
		}
		return new GroupChatUpdateEventSendDto(groupId, messages);
	}

	public void addGroupConversation(String senderId, String senderUsername, String content, String groupId) {
		Group group = groupRepo.findById(groupId).get();
		String convoId = group.getConversationId();
		Conversation convoObject = convoRepo.findById(convoId).get();

		List<String> messages = new ArrayList<String>();

		List<String> messageIds = convoObject.getMessages();
		if (messageIds != null) {
			messages = messageIds;
		}

		String messageId = messageService.addDirectMessage(senderId, senderUsername, content);
		messages.add(messageId);

		convoRepo.save(new Conversation(convoId, convoObject.getParticipants(), messages));
		sendGroupUpdateChatEventByGroupId(groupId);
	}

}
