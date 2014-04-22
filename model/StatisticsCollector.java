package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StatisticsCollector implements MessageVisitor {

	private static final String sentPrivates = "Sent privates";
	private static final String receivedPrivates = "Received privates";
	private static final String privates = "All privates";
	private static final String shortPrivates = "Very short private messages";
	private static final String longPrivates = "Very long private messages";
	
	private static final String theWordWithFused = "The word with F used (Fu@k)";
	private static final String roomMsgs = "Room messages sent";
	private static final String tooLoud = "Too loud messages";
	
	private static final int longMsgMinLength = 100;
	private static final int shortMsgMaxLength = 15;
	private static final String fWord = "fuck";
	
	private String user;
	private Map<String,Integer> statistics;

	public StatisticsCollector(String userName) {
		user = userName;
		statistics = new HashMap<String,Integer>();
		addAllStatistic();
	}

	@Override
	public void visit(PrivateMessage message) {
		incrementSentPrivatesIfNeeded(message);
		incrementReceivedPrivatesIfNeeded(message);
		increaseStatisticValue(privates);
		incrementLongPrivatesIfNeeded(message);
		incrementShortPrivatesIfNeeded(message);
		incrementFWordIfNeeded(message);
		incrementTooLoud(message);
	}

	@Override
	public void visit(RoomMessage message) {
		incrementFWordIfNeeded(message);
		increaseStatisticValue(roomMsgs);
		incrementTooLoud(message);
	}
	
	private void incrementTooLoud(Message message) {
		if(message.getMessage().toUpperCase().equals(message.getMessage())) {
			increaseStatisticValue(tooLoud);
		}
	}
	
	private void incrementFWordIfNeeded(Message message) {
		if(message.getMessage().toLowerCase().contains(fWord)) {
			increaseStatisticValue(theWordWithFused);
		}
	}

	private void incrementLongPrivatesIfNeeded(PrivateMessage message) {
		if(message.getMessage().length() >= longMsgMinLength) {
			increaseStatisticValue(longPrivates);
		}
	}
	
	private void incrementShortPrivatesIfNeeded(PrivateMessage message) {
		if(message.getMessage().length() <= shortMsgMaxLength) {
			increaseStatisticValue(shortPrivates);
		}
	}
	
	private void incrementSentPrivatesIfNeeded(PrivateMessage message) {
		if(message.getFromUserName().equals(user)) {
			increaseStatisticValue(sentPrivates);
		}
	}
	
	private void incrementReceivedPrivatesIfNeeded(PrivateMessage message) {
		if(message.getToUserName().equals(user)) {
			increaseStatisticValue(receivedPrivates);
		}
	}

	private void increaseStatisticValue(String key) {
		Integer value = statistics.get(key);
		statistics.put(key, value + 1);	
	}
	
	private void addAllStatistic() {
		statistics.put(sentPrivates, 0);
		statistics.put(receivedPrivates, 0);
		statistics.put(privates, 0);
		statistics.put(shortPrivates, 0);
		statistics.put(longPrivates, 0);
		statistics.put(theWordWithFused, 0);
		statistics.put(roomMsgs, 0);
		statistics.put(tooLoud, 0);
	}
	
	public List<Statistics> getStatistics() {
		List<Statistics> statisticsList = new LinkedList<Statistics>();
		for(Entry<String,Integer> entry : statistics.entrySet()) {
			statisticsList.add(new Statistics(entry.getKey(), entry.getValue().toString()));
		}
		return statisticsList;
	}

}
