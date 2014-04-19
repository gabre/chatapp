package model;

import java.util.Date;

public abstract class Message extends ChatElement {
	
	private Date when;
	private String fromUserName;
	
	Message(String fromUserName, Date when) {
		this.fromUserName = fromUserName;
		this.when = when;
	}
	
	public abstract void acceptVisitor(MessageVisitor visitor);

	public String getFromUserName() {
		return fromUserName;
	}

	public Date getWhen() {
		return when;
	}
}
