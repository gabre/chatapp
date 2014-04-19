package model;

import java.util.Date;

import model.ModelUtils.EventType;

public class ChatEvent extends ChatElement {

	private Date date;
	private EventType type;
	
	public ChatEvent(Date date, EventType type) {
		this.date = date;
		this.type = type;
	}
	
	public Date getDate() {
		return date;
	}

	public EventType getType() {
		return type;
	}

}
