package model;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	private UserSettings userInfo;
	private ObservableList<String> channels = FXCollections.observableArrayList();
	private ObservableList<String> users = FXCollections.observableArrayList();
	private ObservableList<Message> messages = FXCollections.observableArrayList();
	
	private StatisticsCollector statisticsGenerator;
	private ObservableList<Statistics> statistics = FXCollections.observableArrayList();
	
	public void startConnection(String userName) {
		userInfo = new UserSettings(userName);
	}
	
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
		statisticsGenerator = new StatisticsCollector(userInfo.getUserName());
		for(Message message : messages) {
			message.acceptVisitor(statisticsGenerator);
		}
		statistics.addAll(statisticsGenerator.getStatistics());
		
	}

	public ObservableList<Statistics> getStatistics() {
		return statistics;
	}
}
