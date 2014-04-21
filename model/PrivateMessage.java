package model;

import java.util.Date;

public class PrivateMessage extends Message {

	public PrivateMessage(String fromUserName, Date when, String message) {
		super(fromUserName, when, message);
	}

	@Override
	public void acceptVisitor(MessageVisitor visitor) {
		visitor.visit(this);
	}

}
