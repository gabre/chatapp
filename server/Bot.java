package server;

public class Bot extends ServerUserThread {
	
	boolean running = false;
	
	String name;
	String room;

	Bot(Server server, int id, String name, String room) {
		super(server, id);
		this.name = name;
		this.room = room;
	}
	
	void Update() throws Exception {
	}
	
	void ProcessMessage(String msg) {
	}
	
	void Connect() {
	}
	
	void SendMessage(String msg) {
		server.handle(this, msg);
	}

	public void run() {
		server.handle(this, "CONNECT " + name);
		server.handle(this, "JOIN " + room);
		Connect();
		running = true;
		while (running) {
			try {
				Update();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		server.remove(id);
		close();
	}
	
	public boolean send(String msg) {
		ProcessMessage(msg);
		System.out.println("bot got: " + msg);
		return true;
	}
	
	void close() {
		System.out.println("bot close()");
		running = false;
	}
	
	public void open() {
		System.out.println("bot open()");
	}

}
