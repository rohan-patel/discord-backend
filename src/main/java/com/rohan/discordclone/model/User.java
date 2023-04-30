package com.rohan.discordclone.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Table(value = "users")
public class User {

	@Id
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String userId;

	@Column("email")
	@CassandraType(type = Name.TEXT)
	private String email;

	@Column("username")
	@CassandraType(type = Name.TEXT)
	private String username;

	@Column("password")
	@CassandraType(type = Name.TEXT)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

//	@Column("friends")
//	@CassandraType(type = Name.LIST)
//	private List<String> friends;

	public User(String userId, String email, String username, String password) {
		super();
		this.userId = userId;
		this.email = email;
		this.username = username;
		this.password = password;
//		this.friends = friends;
	}

	public User(String userId, String email, String username) {
		super();
		this.userId = userId;
		this.email = email;
		this.username = username;
	}

	public User() {
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public List<String> getFriends() {
//		return friends;
//	}
//
//	public void setFriends(List<String> friends) {
//		this.friends = friends;
//	}

}
