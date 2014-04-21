package model;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	private ObservableList<String> channels = FXCollections.observableArrayList();
	private ObservableList<String> users = FXCollections.observableArrayList();
	private ObservableList<Message> messages = FXCollections.observableArrayList();
	
	private MessageVisitor statisticsGenerator;
	private ObservableList<Statistics> statistics = FXCollections.observableArrayList();
	
	public ObservableList<String> getChannels() {
		return channels;
	}

	public ObservableList<String> getUsers() {
		return users;
	}

	public ObservableList<Message> getMessages() {
		return messages;
	}

	List<Message> getChatLogs(Filter filter) {
		return null;
		
	}
	
	List<ChatEvent> getChatEvents(Filter filter) {
		return null;
		
	}
	
	List<ChatElement> getHistory(Filter filter) {
		return null;
		
	}
	
	void deleteChatEvent(ChatEvent event) {
		
	}
	
	
	void deleteChatEvents() {
		
	}

	public void calculateStatistics() {
		statistics.clear();
		statistics.add(new Statistics("Statisztika 1", "ertek 1"));
	}

	public ObservableList<Statistics> getStatistics() {
		return statistics;
	}
}
