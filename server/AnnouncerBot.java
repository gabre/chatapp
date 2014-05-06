package server;

public class AnnouncerBot extends Bot {
	
	int interval;
	String msg;

	AnnouncerBot(Server server, int id, String name, int interval, String room, String msg) {
		super(server, id, name, room);
		this.interval = interval;
		this.msg = msg;
	}

	void Update() throws Exception {
		sleep(interval);
		SendMessage("MSG " + room + " " + msg);
	}

}
