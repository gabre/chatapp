package model;

import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	private UserSettings userInfo;
	private ObservableList<String> channels = FXCollections.observableArrayList();
	private ObservableList<String> users = FXCollections.observableArrayList();
	private ObservableList<Message> messages = FXCollections.observableArrayList();
	
	private StatisticsCollector statisticsGenerator;
	private ObservableList<Statistics> statistics = FXCollections.observableArrayList();
	private ObservableList<Entry<String, Integer>> bestFriends = FXCollections.observableArrayList();
	
	public void startConnection(String userName) {
		userInfo = new UserSettings(userName);
		users.add(userName);
	}
	
	public UserSettings getUserInfo() {
		return userInfo;
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
	
	public void deleteChatMessage(Message selectedMessage) {
		messages.remove(selectedMessage);
		calculateStatistics();
	}
	
	public void deleteChatMessages() {
		messages.clear();
		calculateStatistics();
	}

	public void calculateStatistics() {
		statistics.clear();
		statisticsGenerator = new StatisticsCollector(userInfo.getUserName());
		for(Message message : messages) {
			message.acceptVisitor(statisticsGenerator);
		}
		
		statistics.addAll(statisticsGenerator.getStatistics());
		bestFriends.clear();
		bestFriends.addAll(statisticsGenerator.getBestFriends());
	}

	public ObservableList<Statistics> getStatistics() {
		return statistics;
	}
	
	public ObservableList<Entry<String, Integer>> getBestFriends() {
		return bestFriends;
	}

}
