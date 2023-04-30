package com.rohan.discordclone.socket.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.rohan.discordclone.socket.model.message.Message;

public interface MessageRepository extends CassandraRepository<Message, String> {


}
