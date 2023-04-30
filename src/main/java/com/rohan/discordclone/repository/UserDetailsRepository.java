package com.rohan.discordclone.repository;

import java.util.Optional;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import com.rohan.discordclone.model.UserDetails;

public interface UserDetailsRepository extends CassandraRepository<UserDetails, String> {
	
	@AllowFiltering
	Optional<UserDetails> findByEmail(String email);
	
	@AllowFiltering
	Optional<UserDetails> findByUsername(String username);
	
}
