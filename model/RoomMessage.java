package model;

import java.util.Date;

public class RoomMessage extends Message {

	private String roomName;
	
	public RoomMessage(String fromUserName, Date when, String roomName, String message) {
		super(fromUserName, when, message);
		this.roomName = roomName;
	}

	@Override
	public void acceptVisitor(MessageVisitor visitor) {
		visitor.visit(this);
	}

	public String getRoomName() {
		return roomName;
	}
}
