package model;

public class ServerEvent {
	public enum Type {
		USER_JOINED, USER_LEFT, CHAN_CREATED, CHAN_DESTROYED,
		CHAN_MSG, PRIV_MSG
	}
	
	private Type type;
	private String[] args;
	
	public ServerEvent(Type type, String[] args) {
		this.type = type;
		this.args = args;
	}

	public Type getType() {
		return type;
	}

	public String[] getArgs() {
		return args;
	}

}
