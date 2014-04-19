package model;

import java.util.List;

public class Connection {
	
	private String address;
	
	Connection(String address) {
		this.address = address;
	}
	
	void send(Message message) {
		
	}
	
	List<Message> receiveNew() {
		return null;
	}
	
	void close() {
		
	}
	
	ChatEvent connectToRoom(String roomName) {
		return null;
	}
	
	ChatEvent disconnectFromRoom(String roomName) {
		return null;
	}
}
