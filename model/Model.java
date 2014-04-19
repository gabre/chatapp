package model;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	private MessageVisitor statisticsGenerator;
	private Connection connection;
	private ObservableList<Statistics> statistics = FXCollections.observableArrayList();
	
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

	public void connectTo(String text) {
		connection = new Connection(text);
	}

	public void calculateStatistics() {
		statistics.clear();
		statistics.add(new Statistics("Statisztika 1", "ertek 1"));
	}

	public ObservableList<Statistics> getStatistics() {
		return statistics;
	}
}
