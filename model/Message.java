package model;

import java.util.Date;

public abstract class Message extends ChatElement {
	private Date when;
	private String fromUserName;
	private String message;
	
	Message(String fromUserName, Date when, String message) {
		this.fromUserName = fromUserName;
		this.when = when;
		this.message = message;
	}
	
	public abstract void acceptVisitor(MessageVisitor visitor);

	public String getFromUserName() {
		return fromUserName;
	}

	public Date getWhen() {
		return when;
	}
	
	public String getMessage() {
		return message;
	}
}
