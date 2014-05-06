package server;

public class AnnouncerBot extends Bot {
	
	int interval;
	String room;
	String msg;

	AnnouncerBot(Server server, int id, String name, int interval, String room, String msg) {
		super(server, id, name);
		this.interval = interval;
		this.room = room;
		this.msg = msg;
	}
	
	void Connect() {
		server.handle(this, "JOIN " + room);
	}

	void Update() throws Exception {
		sleep(interval);
		SendMessage("MSG " + room + " " + msg);
	}

}
