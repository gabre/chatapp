package server;

public class ReplyBot extends Bot {
	
	ReplyBot(Server server, int id, String name, String room) {
		super(server, id, name, room);
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
				if (message.contains(this.name)) {
					SendMessage("MSG " + roomname + " én?");
				}
			}
		}
	}

}
