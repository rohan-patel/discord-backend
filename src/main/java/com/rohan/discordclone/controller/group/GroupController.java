package com.rohan.discordclone.controller.group;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rohan.discordclone.exception.EntityAlreadyExistsException;
import com.rohan.discordclone.exception.EntityNotFoundException;
import com.rohan.discordclone.model.UserDetails;
import com.rohan.discordclone.model.dto.group.AddParticipantRequestDto;
import com.rohan.discordclone.model.dto.group.CreateGroupRequestDto;
import com.rohan.discordclone.model.dto.group.CreateGroupResponseDto;
import com.rohan.discordclone.repository.UserDetailsRepository;
import com.rohan.discordclone.socket.model.message.Conversation;
import com.rohan.discordclone.socket.model.message.Group;
import com.rohan.discordclone.socket.repository.ConversationRepository;
import com.rohan.discordclone.socket.repository.GroupRepository;
import com.rohan.discordclone.socket.service.SocketGroupService;

@RestController
@RequestMapping("/group")
public class GroupController {
	
	@Autowired
	private GroupRepository groupRepo;
	
	@Autowired
	private ConversationRepository convoRepo;
	
	@Autowired
	private UserDetailsRepository userDetailsRepo;
	
	@Autowired
	private SocketGroupService groupService;
	
	@PostMapping("/create")
	public ResponseEntity<CreateGroupResponseDto> createGroup(@RequestBody CreateGroupRequestDto data) {
		
		String groupName = data.getGroupName();
		String userId = data.getUserId();
		
		String groupId = groupService.createGroup(groupName, userId);
		groupService.sendGroupUpdateEventByUserId(userId);
		
		return new ResponseEntity<>(new CreateGroupResponseDto(groupId, groupName), HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public String addParticipant(@RequestBody AddParticipantRequestDto data) {
		
		String groupId = data.getGroupId();
		String participant = data.getParticipant();
		
		UserDetails userDetails = new UserDetails();
		
//		Checking if User exists
		if (participant.contains(".")) {
			Optional<UserDetails> user = userDetailsRepo.findByEmail(participant);
			if (user.isPresent()) {
				userDetails = user.get();
			}
		} else {
			Optional<UserDetails> user = userDetailsRepo.findByUsername(participant);
			if (user.isPresent()) {
				userDetails = user.get();
			} else {
				throw new EntityNotFoundException("The user with given username or email does not exist");
			}
		}
		
//		Checking if the User is already in the group
		Group group = groupRepo.findById(groupId).get();
		String convoId = group.getConversationId();
		Conversation convoObject = convoRepo.findById(convoId).get();
		
		List<String> participants = convoObject.getParticipants();
		if (participants.contains(userDetails.getUserId())) {
			throw new EntityAlreadyExistsException("The user is already in the group");
		}
		
		groupService.addParticipant(groupId, userDetails.getUserId());
		
		return "Participant Added";
	}

}
