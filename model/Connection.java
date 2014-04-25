package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {
	private Socket socket;
	private BufferedReader input;
	private BufferedWriter output;
	
	public Connection(String address, int port, String name) throws UnknownHostException, IOException {
		socket = new Socket(address, port);
		input = new BufferedReader(
				new InputStreamReader(
						socket.getInputStream()));
		output = new BufferedWriter(
				 new OutputStreamWriter(
						 socket.getOutputStream()));
		send("CONNECT " + name);
	}
	
	public void close() {
		try {
			send("QUIT");
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ServerEvent receive() throws IOException {
		String msg = input.readLine();
		String[] words = msg.split("\\s+");
		
		if (words.length == 0) {
			return receive();
		}
		
		ServerEvent.Type type = null;
		switch (words[0]) {
		case "USER+":
			type = ServerEvent.Type.USER_JOINED;
			break;
		case "USER-":
			type = ServerEvent.Type.USER_LEFT;
			break;
		case "CHAN+":
			type = ServerEvent.Type.CHAN_CREATED;
			break;
		case "CHAN-":
			type = ServerEvent.Type.CHAN_DESTROYED;
			break;
		case "MSG":
			type = ServerEvent.Type.CHAN_MSG;
			break;
		case "PRIVMSG":
			type = ServerEvent.Type.PRIV_MSG;
			break;
		}
		
		if (type != null) {
			return new ServerEvent(type, words);
		} else {
			return receive();
		}
	}
	
	public void sendJoin(String chan) {
		try {
			send("JOIN " + chan);
		} catch (IOException e) {}
	}
	
	public void sendPart(String chan) {
		try {
			send("PART " + chan);
		} catch (IOException e) {}
	}
	
	public void sendChanMessage(String chan, String msg) {
		try {
			send("MSG " + chan + " " + msg);
		} catch (IOException e) {}
	}
	
	public void sendPrivMsg(String to, String msg) {
		try {
			send("PRIVMSG " + to + " " + msg);
		} catch (IOException e) {}
	}
	
	private void send(String msg) throws IOException {
		output.write(msg);
		output.newLine();
		output.flush();
	}
	
}
