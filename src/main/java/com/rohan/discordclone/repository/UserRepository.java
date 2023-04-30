package com.rohan.discordclone.repository;

import java.util.Optional;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import com.rohan.discordclone.model.User;

public interface UserRepository extends CassandraRepository<User, String> {
	
	@AllowFiltering
	Optional<User> findByEmail(String email);
	
	@AllowFiltering
	Optional<User> findByUsername(String username);
	
}
