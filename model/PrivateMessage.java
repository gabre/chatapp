package model;

import java.util.Date;

public class PrivateMessage extends Message {
	private String toUserName;
	
	public PrivateMessage(String fromUserName, String toUserName, Date when, String message) {
		super(fromUserName, when, message);
		this.toUserName = toUserName;
	}

	@Override
	public void acceptVisitor(MessageVisitor visitor) {
		visitor.visit(this);
	}
	
	public String getToUserName() {
		return toUserName;
	}

	@Override
	public String toString() {
		return fromUserName + "~>" + toUserName + "[" + when.toString() + "]: " + message;
	}

}
