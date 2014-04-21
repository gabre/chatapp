package app;

import java.util.Date;

import javafx.application.Platform;
import javafx.concurrent.Task;
import model.Connection;
import model.PrivateMessage;
import model.RoomMessage;
import model.ServerEvent;

public class ConnectionListener extends Task<Void> {
	private Connection conn;
	private ChatClientApplication app;
	
	public ConnectionListener(Connection conn, ChatClientApplication app) {
		this.conn = conn;
		this.app = app;
	}

	private String unwordsFrom(int k, String[] args) {
		String str = "";
		for (int i = k; i < args.length; ++i) {
			str += args[i];
			if (i != args.length - 1) str += " ";
		}
		return str;
	}

	@Override
	protected Void call() throws Exception {
		try {
			while (true) {
				final ServerEvent event = conn.receive();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						switch (event.getType()) {
						case USER_JOINED:
							if (event.getArgs().length == 2) {
								app.getModel().getUsers().add(event.getArgs()[1]);
							}
							break;
						case USER_LEFT:
							if (event.getArgs().length == 2) {
								app.getModel().getUsers().remove(event.getArgs()[1]);
							}
							break;
						case CHAN_CREATED:
							app.getModel().getChannels().add(event.getArgs()[1]);
							break;
						case CHAN_DESTROYED:
							app.getModel().getChannels().remove(event.getArgs()[1]);
							break;
						case CHAN_MSG: {
							String chan = event.getArgs()[1];
							String from = event.getArgs()[2];
							String msg = unwordsFrom(3, event.getArgs());
							app.getModel().getMessages().add(new RoomMessage(from, new Date(), chan, msg));
							break;
						}
						case PRIV_MSG: {
							String from = event.getArgs()[1];
							String msg = unwordsFrom(2, event.getArgs());
							app.getModel().getMessages().add(new PrivateMessage(from, new Date(), msg));
							break;
						}
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			app.onConnectionLost();
		}
		return null;
	}

}
