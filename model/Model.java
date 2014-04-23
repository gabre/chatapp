package model;

import java.util.List;
import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	private UserSettings userInfo;
	private ObservableList<String> channels = FXCollections.observableArrayList();
	private ObservableList<String> users = FXCollections.observableArrayList();
	private ObservableList<Message> messages = FXCollections.observableArrayList();
	private ObservableList<Message> messagesModel;
	
	private StatisticsCollector statisticsGenerator;
	private ObservableList<Statistics> statistics = FXCollections.observableArrayList();
	private ObservableList<Entry<String, Integer>> bestFriends = FXCollections.observableArrayList();
	
	public void startConnection(String userName) {
		userInfo = new UserSettings(userName);
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
		messagesModel = FXCollections.observableArrayList(messages);
		return messagesModel;
	}

	public void applyNoFilter() {
		messagesModel.clear();
		messagesModel.addAll(messages);
	}
	
	public void filterPrivates() {
		for(Message message : messages) {
			if(!(message instanceof PrivateMessage)) {
				messagesModel.remove(message);
			}
		}
	}
	
	public void filterRoomMessages() {
		for(Message message : messages) {
			if(!(message instanceof RoomMessage)) {
				messagesModel.remove(message);
			}
		}		
	}
	
	public void deleteChatEvent(Message selectedMessage) {
		messages.remove(selectedMessage);
	}
	
	
	public void deleteChatEvents() {
		messages.clear();
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
