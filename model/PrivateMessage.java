package model;

import java.util.Date;

public class PrivateMessage extends Message {

	private String toUserName;
	
	PrivateMessage(String fromUserName, Date when, String toUserName) {
		super(fromUserName, when);
		this.toUserName = toUserName;
	}

	@Override
	public void acceptVisitor(MessageVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	public String getToUserName() {
		return toUserName;
	}
}
