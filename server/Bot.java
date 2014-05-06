package server;

public class Bot extends ServerUserThread {
	
	boolean running = false;

	Bot(Server server, int id) {
		super(server, id);
	}
	
	void Update() throws Exception {
	}

	public void run() {
		server.handle(this, "CONNECT bot");
		server.handle(this, "JOIN a");
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
