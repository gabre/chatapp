package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerUserThread extends Thread {
	Socket socket;
	Server server;
	int id = -1;
	String username = "";
	private BufferedReader streamIn = null;
	private BufferedWriter streamOut = null;

	public int getID() {
		return id;
	}
	
	ServerUserThread(Server server, int id) {
		this.socket = null;
		this.server = server;
		this.id = id;
	}
	
	ServerUserThread(Server server, Socket socket) {
		this.socket = socket;
		this.server = server;
		id = socket.getPort();
	}

	public void open() {
		try	{
			streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			streamOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		}
		catch (IOException e) {
			System.out.println("Exception creating new Input/output Streams: " + e);
			return;
		}
	}
	
	public void run() {
		boolean running = true;
		while(running) {
			try {
				String in = streamIn.readLine();
				if(in != null)
					server.handle(this, in);
			}
			catch (IOException e) {
				System.out.println(id + " Exception reading Streams: " + e);
				break;				
			}
		}
		server.remove(id);
		close();
	}

	void close() {
		try {
			if (socket != null)
				socket.close();
			if (streamIn != null)
				streamIn.close();
			if (streamOut != null)
				streamOut.close();
		}
		catch (Exception e) {}
	}

	public boolean send(String msg) {
		if(!socket.isConnected()) {
			close();
			return false;
		}
		try {
			streamOut.write(msg);
			streamOut.newLine();
			streamOut.flush();
		}

		catch(IOException e) {
			System.out.println("Error messaging " + id + ": " + e.toString());
			server.remove(id);
		}
		return true;
		
	}
}
