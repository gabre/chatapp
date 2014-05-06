package server;

public class AnnouncerBot extends Bot {

	AnnouncerBot(Server server, int id) {
		super(server, id);
	}
	
	void Update() throws Exception {
		sleep(1000);
		String in = "MSG a szia";
		server.handle(this, in);
	}

}
