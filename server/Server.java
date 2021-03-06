package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	private ArrayList<ServerUserThread> ServerUserThreads;
	private int port;
	private boolean running;
	private HashMap<String, ServerUserThread> users;
	private HashMap<String, ArrayList<String>> rooms;
	private ArrayList<String> serverlog;
		
	public Server(int port) {
		this.port = port;
		ServerUserThreads = new ArrayList<ServerUserThread>();
		users = new HashMap<String, ServerUserThread>();
		rooms = new HashMap<String, ArrayList<String>>();
		serverlog = new ArrayList<String>();
	}
	
	private void AddBots() {
		ServerUserThread bot = new AnnouncerBot(this, 23456, "AnnouncerBot", 5000, "ann", "szia");
		ServerUserThreads.add(bot);
		bot.start();
		
		bot = new EchoBot(this, 23457, "EchoBot", "echo");
		ServerUserThreads.add(bot);
		bot.start();
		
		bot = new ReplyBot(this, 23458, "ReplyBot", "reply");
		ServerUserThreads.add(bot);
		bot.start();
		
		bot = new GameBot(this, 23459, "GameBot", "game");
		ServerUserThreads.add(bot);
		bot.start();
	}
	
	public void start() {
		running = true;
		try	{
			ServerSocket serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);

			AddBots();

			while(running) {				
				Socket socket = serverSocket.accept();
				if(!running)
					break;
				ServerUserThread thread = new ServerUserThread(this, socket);
				ServerUserThreads.add(thread);
				thread.open();
				thread.start();
			}
			try {
				serverSocket.close();
				for(ServerUserThread thread : ServerUserThreads) {
					thread.close();
				}
			}
			catch(Exception e) {
				System.out.println("Exception closing the server and clients: " + e);
			}
		}
		catch (IOException e) {
            String msg = " Exception on new ServerSocket: " + e + "\n";
            System.out.println(msg);
            e.printStackTrace();
		}
	}

	private synchronized void sendRoom(String roomname, String message, int sender) {
		Iterator<String> iter = rooms.get(roomname).iterator();
		while(iter.hasNext())
		{
			ServerUserThread thread = users.get(iter.next());
			if (thread == null || (thread.getID() != sender && !thread.send(message)))
				iter.remove();
		}
	}
	
	private synchronized void broadcast(String message, int sender) {
		Iterator<ServerUserThread> iter = users.values().iterator();
		while(iter.hasNext())
		{
			ServerUserThread thread = iter.next();
			if(	thread.getID() != sender && !thread.send(message))
				iter.remove();
		}
	}

	synchronized void remove(int id) {
		Iterator<ServerUserThread> iter = ServerUserThreads.iterator();
		while(iter.hasNext()) {
			ServerUserThread thread = iter.next();
			if(thread.getID() == id) {
				iter.remove();
				return;
			}
		}
	}
	
	private synchronized boolean addUser(String username, ServerUserThread thread) {
		if(users.containsKey(username))
			return false;
		thread.username = username;
		users.put(username, thread);
		return true;
	}

	
	public synchronized void handle(ServerUserThread client, String input) {
		// READABLE CODE 101
		serverlog.add(client.getID() + ":" + input);
		System.out.println(client.getID() + ":" + input);
		
		if (input.startsWith("CONNECT ")) {
			String username = input.substring(8).trim();
			
			if(!validName(username))
				client.send("SRVFATAL Felhaszn�l�n�v csak a k�vetkez� bet�kb�l �llhat: a-z, A-Z, 0-9, _ !");
			else if(users.containsKey(username))
				client.send("SRVFATAL Felhaszn�l�n�v haszn�latban!");
			else
			{
				broadcast("USER+ " + username, client.getID());
				
				String userlist = new String();			
				for(String user : users.keySet())
					userlist += "USER+ " + user + "\n";
				client.send(userlist);
				
				for (String room : rooms.keySet())
					client.send("CHAN+ " + room);
				
				addUser(username, client);
			}
		}
		else if(input.equals("QUIT")) {
			users.remove(client.username);
			remove(client.getID());
			broadcast("USER- " + client.username, client.getID());
		}
		else if(input.startsWith("JOIN ")) {
			String roomname = input.substring(5).trim();
			
			if(!validName(roomname))
				client.send("SRVERR Szoban�v csak a k�vetkez� bet�kb�l �llhat: a-z, A-Z, 0-9, _ !");
			else {
				if(!rooms.containsKey(roomname)) {
					rooms.put(roomname, new ArrayList<String>());
					broadcast("CHAN+ " + roomname, -1);
				}
				
				if(!rooms.get(roomname).contains(client.username))
				{
					rooms.get(roomname).add(client.username);
					sendRoom(roomname, "USER+ " + roomname + " " + client.username, client.getID());
				}
			}
		}
		else if(input.startsWith("PART ")) {
			String roomname = input.substring(5).trim();
			
			if(!rooms.containsKey(roomname))
				client.send("SRVERR Szoba nem l�tezik!");
			else if (!rooms.get(roomname).contains(client.username))
				client.send("SRVERR Nem a szoba tagja!");
			else {
				rooms.get(roomname).remove(client.username);
				sendRoom(roomname, "USER- " + roomname + client.username, client.getID());
				if(rooms.get(roomname).size() == 0) {
					rooms.remove(roomname);
					broadcast("CHAN- " + roomname, client.getID());
				}
			}
		}
		else if(input.startsWith("MSG ")) {
			String commandSuffix = input.substring(4).trim();
			int firstSpace = commandSuffix.indexOf(' ');
			if (firstSpace == -1)
				client.send("SRVERR Nincs �zenet megadva!");
			else {
				String roomname = commandSuffix.substring(0, firstSpace);
				String message = commandSuffix.substring(firstSpace+1);
				if(!rooms.containsKey(roomname))
					client.send("SRVERR Szoba nem l�tezik!");
				else
					sendRoom(roomname, "MSG " + roomname + " " + client.username + " " + message, client.getID());
			}
		}
		else if(input.startsWith("PRIVMSG ")) {
			String commandSuffix = input.substring(8).trim();
			int firstSpace = commandSuffix.indexOf(' ');
			if (firstSpace == -1)
				client.send("SRVERR Nincs �zenet megadva!");
			else {
				String username = commandSuffix.substring(0, firstSpace);
				String message = commandSuffix.substring(firstSpace+1);
				if(!users.containsKey(username))
					client.send("SRVERR Felhaszn�l� nem l�tezik!");
				else
					users.get(username).send("PRIVMSG " + client.username + " " + message);
			}
		}
		else if(input.equals("USERLIST")) {
			String userlist = new String();			
			for(String user : users.keySet())
				userlist += user + " ";
			client.send(userlist);
		}
	}
		
	private boolean validName(String username) {
		for(int i = 0; i < username.length(); ++i)
		{
			char ch = username.charAt(i);
			if(!('a' <= ch && ch <= 'z' ||
			     'A' <= ch && ch <= 'Z' ||
			     '0' <= ch && ch <= '9' ||
			     '_' == ch))
					return false;
		}
		return true;
	}

	public static void main(String[] args) {
		int portNumber = 12345;
		switch(args.length) {
			case 1:
				try {
					portNumber = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					System.out.println("Haszn�lat: > java Server [portsz�m]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Haszn�lat: > java Server [portsz�m]");
				return;
				
		}
		Server server = new Server(portNumber);
		server.start();
	}
}

