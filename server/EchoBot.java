package server;

public class EchoBot extends Bot {
	
	String room;

	EchoBot(Server server, int id, String name, String room) {
		super(server, id, name);
		this.room = room;
	}
	
	void Connect() {
		server.handle(this, "JOIN " + room);
	}

	void ProcessMessage(String msg) {
		if (msg.startsWith("MSG ")) {
			String commandSuffix = msg.substring(4).trim();
			int firstSpace = commandSuffix.indexOf(' ');
			if (firstSpace == -1)
				return;
			else {
				String roomname = commandSuffix.substring(0, firstSpace);
				String message = commandSuffix.substring(firstSpace+1);
				SendMessage("MSG " + roomname + " " + message);
			}
		}
	}

}
