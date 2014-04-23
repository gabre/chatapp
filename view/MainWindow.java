package view;

import java.util.Date;
import java.util.HashMap;

import model.Message;
import model.MessageVisitor;
import model.PrivateMessage;
import model.RoomMessage;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import app.ChatClientApplication;

public class MainWindow extends Window {
	private class Session {
		public boolean roomMsg;
		public String name;
		public Session(boolean roomMsg, String name) {
			this.roomMsg = roomMsg;
			this.name = name;
		}
		@Override
		public boolean equals(Object obj) {
			Session s = (Session) obj;
			return roomMsg == s.roomMsg && name.equals(s.name);
		}
		@Override
		public int hashCode() {
			return name.hashCode() * (roomMsg ? 1 : 2);
		}
	}
	
	private ListView<String> lvChannels, lvUsers;
	private Label lbNickname;
	private TextField tfMessage;
	private HashMap<Session, ListView<String>> sessions;
	
	private TabPane messageTabs;

	public MainWindow(ChatClientApplication app) {
		super(app);
		
		lvChannels = new ListView<>(app.getModel().getChannels());	
		lvUsers = new ListView<>(app.getModel().getUsers());
		lbNickname = new Label();
		tfMessage = new TextField();
		sessions = new HashMap<>();
		messageTabs = new TabPane();
	}

	@Override
	public String getTitle() {
		return new String("Chat kliens");
	}

	@Override
	public Parent getView() {
		BorderPane mainPane = new BorderPane();
		
		messageTabs.setPrefSize(400, 400);
		mainPane.setCenter(messageTabs);
		
		VBox rightBox = new VBox(5);
		rightBox.getChildren().addAll(
				lbNickname,
				new Label("Csatornák"),
				lvChannels,
				new Label("Felhasználók"),
				lvUsers);
		rightBox.setPrefHeight(400);
		mainPane.setRight(rightBox);
		
		HBox bottomBox = new HBox(5);
		
		Button btnHistory = new Button("Napló");
		Button btnSend = new Button("Küldés");
		tfMessage.setMaxWidth(Double.MAX_VALUE);
		bottomBox.getChildren().addAll(
				tfMessage,
				btnSend,
				btnHistory);
		HBox.setHgrow(tfMessage, Priority.ALWAYS);
		mainPane.setBottom(bottomBox);
		
		BorderPane.setMargin(messageTabs, new Insets(0, 5, 0, 0));
		BorderPane.setMargin(bottomBox, new Insets(5, 0, 0, 0));
		mainPane.setPadding(new Insets(5));
		
		btnHistory.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				app.onHistoryWindowOpened();
			}
		});
		
		EventHandler<ActionEvent> sendAction = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				send();
			}
		};
		btnSend.setOnAction(sendAction);
		tfMessage.setOnAction(sendAction);
		
		lvChannels.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					String chan = lvChannels.getSelectionModel().getSelectedItem();
					if (chan == null) return;
					Session s = new Session(true, chan);
					messageListFor(s);
					messageTabs.getSelectionModel().select(getTabFor(s));
				}
			}
		});
		
		lvUsers.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					String user = lvUsers.getSelectionModel().getSelectedItem();
					if (user == null) return;
					Session s = new Session(false, user);
					messageListFor(s);
					messageTabs.getSelectionModel().select(getTabFor(s));
				}
			}
		});
		
		return mainPane;
	}

	public void onMessage(Message msg) {
		System.out.println(msg.toString());
		class TmpVisitor implements MessageVisitor {
			public Session s;
			public String from;
			@Override
			public void visit(PrivateMessage message) {
				if (message.getToUserName().equals(app.getModel().getUserInfo().getUserName())) {
					s = new Session(false, message.getFromUserName());
				} else {
					s = new Session(false, message.getToUserName());
				}
				from = message.getFromUserName();
			}
			@Override
			public void visit(RoomMessage message) {
				s = new Session(true, message.getRoomName());
				from = message.getFromUserName();
			}
		};
		TmpVisitor v = new TmpVisitor();
		msg.acceptVisitor(v);
		ListView<String> lv = messageListFor(v.s);
		lv.getItems().add(msg.getWhen().toString() + " <" + v.from + "> " + msg.getMessage());
	}
	
	public void setDisplayedUsername(String str) {
		lbNickname.setText(str);
	}
	
	private ListView<String> messageListFor(final Session s) {
		ListView<String> lv = sessions.get(s);
		if (lv == null) {
			lv = new ListView<>();
			sessions.put(s, lv);
			Tab tab = new Tab();
			tab.setText(s.name);
			tab.setContent(lv);
			tab.setUserData(s);
			tab.setOnClosed(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					sessions.remove(s);
					if (s.roomMsg) {
						app.getConn().sendPart(s.name);
					}
				}
			});
			messageTabs.getTabs().add(tab);
			if (s.roomMsg) {
				app.getConn().sendJoin(s.name);
			}
		}
		return lv;
	}
	
	private Tab getTabFor(Session s) {
		for (Tab tab : messageTabs.getTabs()) {
			if (tab.getUserData().equals(s)) {
				return tab;
			}
		}
		return null;
	}
	
	private void send() {
		Tab tab = messageTabs.getSelectionModel().getSelectedItem();
		String msg = tfMessage.getText();
		if (tab == null || msg.isEmpty()) return;
		Session s = (Session) tab.getUserData();
		String me = app.getModel().getUserInfo().getUserName();
		Message msgObj = s.roomMsg
				? new RoomMessage(me, new Date(), s.name, msg)
		        : new PrivateMessage(me, s.name, new Date(), msg);
		app.send(msgObj);
		tfMessage.setText("");
	}

}
