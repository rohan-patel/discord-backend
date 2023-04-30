package com.rohan.discordclone.socket.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.rohan.discordclone.socket.model.ConnectedUsers;
import com.rohan.discordclone.socket.model.dto.ChatUpdateEventSendDto;
import com.rohan.discordclone.socket.model.message.Conversation;
import com.rohan.discordclone.socket.model.message.Message;
import com.rohan.discordclone.socket.repository.ConversationRepository;
import com.rohan.discordclone.socket.repository.MessageRepository;

@Service
public class SocketMessagingService {

	@Autowired
	private MessageRepository messageRepo;

	@Autowired
	private ConversationRepository convoRepo;

	public String addDirectMessage(String authorId, String authorUsername, String content) {

		String messageId = UUID.randomUUID().toString();
		Message message = new Message(messageId, authorId, authorUsername, content, LocalDateTime.now().toString(), "DIRECT");

//		System.out.println("Adding Message: ");
//		System.out.println("Message ID: " + message.getMessageId());
//		System.out.println("Sender ID: " + message.getAuthorId());
//		System.out.println("Sender USername: " + message.getAuthorUsername());
//		System.out.println("Content: " + message.getContent());
//		System.out.println("Date: " + message.getDate());
//		System.out.println("Type: " + message.getType());
//		System.out.println();

		messageRepo.save(message);
		return messageId;
	}

	public String addConversation(String authorId, String authorUsername, String content, String receiverId) {

		String conversationId = getConversationId(authorId, receiverId);
		Optional<Conversation> convoObject = convoRepo.findById(conversationId);

		List<String> prevMessages = new ArrayList<String>();
		List<String> participants = new ArrayList<String>();

		if (convoObject.isPresent()) {
			Conversation convo = convoObject.get();
			List<String> messages = convo.getMessages();
			List<String> loadParticipants = convo.getParticipants();
			participants = loadParticipants;
			prevMessages = messages;
		}

		String messageId = addDirectMessage(authorId, authorUsername, content);

		prevMessages.add(messageId);

		if (participants.size() < 2) {
			participants.clear();
			participants.add(authorId);
			participants.add(receiverId);
		}

//		System.out.println("Adding Conversation");
//		System.out.println("convoID: " + conversationId);
//		System.out.print("Participants: ");
//		System.out.println(participants);
//		System.out.print("Messages: ");
//		System.out.println(prevMessages);

		convoRepo.save(new Conversation(conversationId, participants, prevMessages));

		sendUpdateChatEvent(authorId, receiverId, conversationId);

		return conversationId;
	}

	public String getConversationId(String senderId, String receiverId) {
		int comparison = senderId.compareTo(receiverId);

		String result = comparison < 0 ? senderId + receiverId : receiverId + senderId;

		UUID uuid = UUID.nameUUIDFromBytes(result.getBytes());

		return uuid.toString();
	}

	public void sendUpdateChatEvent(String senderId, String receiverId, String conversationId) {

		sendUpdateChatEventByUserId(senderId, conversationId);
		sendUpdateChatEventByUserId(receiverId, conversationId);

	}

	public void sendUpdateChatEventByUserId(String userId, String convoId) {

		Optional<Conversation> optionalConvo = convoRepo.findById(convoId);
		List<Message> messages = new ArrayList<Message>();
		List<String> participants = new ArrayList<String>();

		if (optionalConvo.isPresent()) {
			Conversation convo = optionalConvo.get();

			List<String> messageIds = convo.getMessages();
			List<String> loadedParticipants = convo.getParticipants();
			participants = loadedParticipants;
			List<Message> loadedMessages = messageRepo.findAllById(messageIds);
			messages = loadedMessages;
		}
		ChatUpdateEventSendDto eventSendDto = new ChatUpdateEventSendDto(messages, participants);

		Map<SocketIOClient, String> map = ConnectedUsers.getConnectedUsers();

		Map<SocketIOClient, String> userIdSocketClients = map.entrySet().stream()
				.filter(x -> Arrays.asList(userId).contains(x.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

//		System.out.println("");
//		System.out.println("Printing SocketIDs");
		userIdSocketClients.forEach((socketClient, user) -> {
//			System.out.println(socketClient.getSessionId().toString());
			socketClient.sendEvent("chat-update-history", eventSendDto);
		});
	}

}
