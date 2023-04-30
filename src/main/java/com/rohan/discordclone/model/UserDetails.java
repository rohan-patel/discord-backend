package com.rohan.discordclone.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "user_details")
public class UserDetails {

	@Id
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String userId;

	@Column("email")
	@CassandraType(type = Name.TEXT)
	private String email;

	@Column("username")
	@CassandraType(type = Name.TEXT)
	private String username;

	@Column("groups")
	@CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
	private List<String> groups;

	@Column("friends")
	@CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
	private List<String> friends;

	public UserDetails(String userId, String email, String username, List<String> groups, List<String> friends) {
		super();
		this.userId = userId;
		this.email = email;
		this.username = username;
		this.groups = groups;
		this.friends = friends;
	}

	public UserDetails(String userId, String email, String username, List<String> friends) {
		super();
		this.userId = userId;
		this.email = email;
		this.username = username;
		this.friends = friends;
	}

	public UserDetails(String userId, String username) {
		super();
		this.userId = userId;
		this.username = username;
	}

	public UserDetails(String userId, String username, String email) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
	}

	public UserDetails() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

}
