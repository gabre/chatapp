package model;

import java.util.Date;

public class RoomMessage extends Message {

	private String roomName;
	
	RoomMessage(String fromUserName, Date when, String roomName) {
		super(fromUserName, when);
		this.roomName = roomName;
	}

	@Override
	public void acceptVisitor(MessageVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	public String getRoomName() {
		return roomName;
	}
}
