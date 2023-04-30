package com.rohan.discordclone.socket.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;

@Service
public class ConnectedUsers {

	private static Map<SocketIOClient, String> connectedUsers = new HashMap<>();

	public static Map<SocketIOClient, String> getConnectedUsers() {
		return connectedUsers;
	}

	public static boolean addUser(SocketIOClient client, String principal) {

		try {
//			if (!(connectedUsers.containsValue(principal) || connectedUsers.containsKey(socketId))) {
			if (!(connectedUsers.containsKey(client))) {
				connectedUsers.put(client, principal);
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static boolean removeUser(SocketIOClient client) {
		try {
			connectedUsers.remove(client);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getUser(SocketIOClient client) {

		if (connectedUsers.containsKey(client)) {
			return connectedUsers.get(client);
		}
		return "";
	}

	public static void setConnectedUsers(Map<SocketIOClient, String> connectedUsers) {
		ConnectedUsers.connectedUsers = connectedUsers;
	}

}
