package server;

import java.util.Random;

public class GameBot extends Bot {
	
	boolean started;
	int number;

	GameBot(Server server, int id, String name, String room) {
		super(server, id, name, room);
		started = false;
		number = 0;
	}
	
	void StartGame() {
		started = true;
		Random r = new Random();
		number = r.nextInt(1000);
		SendMessage("MSG " + room + " tippelj egy számot 'guess x'");
	}

	void Guess(int guess) {
		if (guess == number) {
			started = false;
			SendMessage("MSG " + room + " nyertél!");
		} else if (guess < number) {
			SendMessage("MSG " + room + " annál több");
		} else if (guess > number) {
			SendMessage("MSG " + room + " annál kevesebb");
		}
	}

	void ProcessMessage(String msg) {
		if (msg.startsWith("MSG ")) {
			String commandSuffix = msg.substring(4).trim();
			int firstSpace = commandSuffix.indexOf(' ');
			if (firstSpace == -1)
				return;
			else {
				String message = commandSuffix.substring(firstSpace+1);
				firstSpace = message.indexOf(' ');
				message = message.substring(firstSpace+1);
				if (message.startsWith("game start") && !started) {
					StartGame();
				}
				if (message.startsWith("guess ") && started) {
					try {
						int num = Integer.parseInt(message.substring(6));
						Guess(num);
					} catch (NumberFormatException e) {
						SendMessage("MSG " + room + " mi?");
					}
				}
			}
		}
	}

}
