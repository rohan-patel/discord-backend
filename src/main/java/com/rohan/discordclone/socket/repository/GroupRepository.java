package com.rohan.discordclone.socket.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.rohan.discordclone.socket.model.message.Group;

public interface GroupRepository extends CassandraRepository<Group, String> {


}
