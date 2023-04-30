package com.rohan.discordclone.controller.invitefriends;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rohan.discordclone.exception.EntityNotFoundException;
import com.rohan.discordclone.exception.InvalidFriendInviteException;
import com.rohan.discordclone.model.FriendInvitation;
import com.rohan.discordclone.model.User;
import com.rohan.discordclone.model.UserDetails;
import com.rohan.discordclone.model.dto.friendinvitation.InviteFriendDto;
import com.rohan.discordclone.repository.FriendInvitationRepository;
import com.rohan.discordclone.repository.UserDetailsRepository;
import com.rohan.discordclone.repository.UserRepository;
import com.rohan.discordclone.socket.model.dto.InviteDecisionsDto;
import com.rohan.discordclone.socket.service.SocketService;

@RestController
@RequestMapping("/friend-invitation")
public class InviteFriendsController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private FriendInvitationRepository invitationRepo;

	@Autowired
	private UserDetailsRepository userDetailsRepo;

	@Autowired
	private SocketService socketService;

	@PostMapping("/invite")
	public String inviteFriends(@RequestBody InviteFriendDto data) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User authPrincipal = (User) auth.getPrincipal();

		String principal = data.getPrincipal();

		Optional<User> targetUser;

//		Checking if target principal is not the user itself
//		and if the target principal exists
		if (principal.contains(".")) {
			if (principal.equals(authPrincipal.getEmail())) {
				throw new InvalidFriendInviteException("Sorry. You cannot become friend of yourself.");
			}
			targetUser = userRepo.findByEmail(principal);
			if (targetUser.isEmpty()) {
				throw new EntityNotFoundException("The user you are trying to invite doesn't exist");
			}

		} else {
			if (principal.equals(authPrincipal.getUsername())) {
				throw new InvalidFriendInviteException("Sorry. You cannot become friend of yourself.");
			}
			targetUser = userRepo.findByUsername(principal);
			if (targetUser.isEmpty()) {
				throw new EntityNotFoundException("The user you are trying to invite doesn't exist");
			}
		}

//		Checking if the targetUser is already a friend
		UserDetails userDetails = userDetailsRepo.findById(authPrincipal.getUserId()).get();
		List<String> friends = userDetails.getFriends();
		if (friends != null && friends.contains(targetUser.get().getUserId())) {
			throw new InvalidFriendInviteException("The user you want to send invite is already your friend");
		}

//		Checking if the invitation has already been sent
		if (invitationRepo.existsBySenderIdAndReceiverId(authPrincipal.getUserId(), targetUser.get().getUserId())) {
			throw new InvalidFriendInviteException("Invitation has been already sent");
		}

//		Create a new invitation in the database
		FriendInvitation friendInvitation = new FriendInvitation(UUID.randomUUID().toString(),
				authPrincipal.getUserId(), targetUser.get().getUserId());
		invitationRepo.save(friendInvitation);

//		After creating a new invitation we need to send an event to the
//		Receiver ID to all the socket connections he is connected to
		socketService.sendPendingInvitationsEventByUserId(targetUser.get().getUserId());

		return "Invite Sent";
	}

	@PostMapping("/accept")
	public String acceptInvite(@RequestBody InviteDecisionsDto sender) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User authPrincipal = (User) auth.getPrincipal();

		String receiverId = authPrincipal.getUserId();
		String senderId = sender.getId();

//		Updating Friends List of Sender
		UserDetails senderUserDetails = userDetailsRepo.findById(senderId).get();
		List<String> senderFriends = senderUserDetails.getFriends();
		if (senderFriends == null) {
			List<String> friends = new ArrayList<String>();
			friends.add(receiverId);
			senderFriends = friends;
		} else {
			senderFriends.add(receiverId);
		}
		userDetailsRepo.save(new UserDetails(senderUserDetails.getUserId(), senderUserDetails.getEmail(),
				senderUserDetails.getUsername(), senderUserDetails.getGroups(), senderFriends));

//		Updating Friends List of Receiver
		UserDetails receiverUserDetails = userDetailsRepo.findById(receiverId).get();
		List<String> receiverFriends = receiverUserDetails.getFriends();
		if (receiverFriends == null) {
			List<String> friends = new ArrayList<String>();
			friends.add(senderId);
			receiverFriends = friends;
		} else {
			receiverFriends.add(senderId);
		}
		userDetailsRepo.save(new UserDetails(receiverUserDetails.getUserId(), receiverUserDetails.getEmail(),
				receiverUserDetails.getUsername(), receiverUserDetails.getGroups(), receiverFriends));
		
//		Deleting Invitation Object from Pending Invitation Table
		Optional<FriendInvitation> invitationObject = invitationRepo.findBySenderIdAndReceiverId(senderId, receiverId);

		if (invitationObject.isPresent()) {
			invitationRepo.deleteById(invitationObject.get().getInvitationId());
		}

//		Sending event to frontend for updating pending invitation list
		socketService.sendPendingInvitationsEventByUserId(receiverId);
		
//		Sending an event to both users to update friends in frontend
		socketService.sendFriendsEventByUserId(senderId);
		socketService.sendFriendsEventByUserId(receiverId);

		return "Invite Accepted";
	}

	@PostMapping("/reject")
	public String rejectInvite(@RequestBody InviteDecisionsDto sender) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User authPrincipal = (User) auth.getPrincipal();

		String receiverId = authPrincipal.getUserId();
		String senderId = sender.getId();

//		Deleting Invitation Object from Pending Invitation Table
		Optional<FriendInvitation> invitationObject = invitationRepo.findBySenderIdAndReceiverId(senderId, receiverId);

		if (invitationObject.isPresent()) {
			invitationRepo.deleteById(invitationObject.get().getInvitationId());
		}

//		Sending event to frontend for updating pending invitation list
		socketService.sendPendingInvitationsEventByUserId(receiverId);

		return "Invite Rejected";
	}
	

	@GetMapping("/test")
	public String friendTest() {
		return "Test Successfull";
	}

}
