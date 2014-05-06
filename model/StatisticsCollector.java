package model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StatisticsCollector implements MessageVisitor {

	public static final String statisticSentPrivates = "Elküldött privát üzenetek";
	public static final String statisticReceivedPrivates = "Fogadott privát üzenetek";
	public static final String statisticPrivates = "Összes privát üzenet";
	public static final String statisticShortPrivates = "Rövid privát üzenetek";
	public static final String statisticLongPrivates = "Hosszú privát üzenetek";
	
	public static final String statisticTheWordWithFused = "Az F betûs szó használata (franc)";
	public static final String statisticRoomMsgs = "Elküldött szobaüzenetek";
	public static final String statisticTooLoud = "Kiabálás";
	
	private static final int longMsgMinLength = 100;
	private static final int shortMsgMaxLength = 15;
	private static final String fWord = "franc";
	
	private String user;
	private Map<String,Integer> statistics;
	private Map<String,Integer> msgPerUser;

	public StatisticsCollector(String userName) {
		user = userName;
		statistics = new HashMap<String,Integer>();
		msgPerUser = new HashMap<String,Integer>();
		addAllStatistic();
	}

	@Override
	public void visit(PrivateMessage message) {
		incrementSentPrivatesIfNeeded(message);
		incrementReceivedPrivatesIfNeeded(message);
		increaseStatisticValue(statisticPrivates);
		incrementLongPrivatesIfNeeded(message);
		incrementShortPrivatesIfNeeded(message);
		incrementFWordIfNeeded(message);
		incrementTooLoud(message);
		increaseFriendMsgCount(message);
	}
	
	@Override
	public void visit(RoomMessage message) {
		incrementFWordIfNeeded(message);
		increaseStatisticValue(statisticRoomMsgs);
		incrementTooLoud(message);
	}
	
	public List<Statistics> getStatistics() {
		List<Statistics> statisticsList = new LinkedList<Statistics>();
		for(Entry<String,Integer> entry : statistics.entrySet()) {
			statisticsList.add(new Statistics(entry.getKey(), entry.getValue().toString()));
		}
		return statisticsList;
	}
	
	public List<Entry<String,Integer>> getBestFriends() {
		Comparator<Entry<String, Integer>> comparator = new Comparator<Entry<String, Integer>>() {
		    @Override
		    public int compare(final Entry<String, Integer> x, final Entry<String, Integer> y){
		        return x.getValue() - y.getValue();
		    }
		};
		List<Entry<String, Integer>> friends = new LinkedList<Entry<String, Integer>>(msgPerUser.entrySet());
		Collections.sort(friends, comparator);
		
		return friends;
	}

	private void increaseFriendMsgCount(PrivateMessage message) {
		if(message.getFromUserName().equals(user)) {
			increaseFriendMsgCountWithName(message.getToUserName());
		} else {
			increaseFriendMsgCountWithName(message.getFromUserName());
		}
	}
	
	private void increaseFriendMsgCountWithName(String name) {
		Integer value = msgPerUser.get(name);
		if(value != null) {
			msgPerUser.put(name, value + 1);
		} else {
			msgPerUser.put(name, 1);
		}
	}
	
	private void incrementTooLoud(Message message) {
		if(message.getMessage().toUpperCase().equals(message.getMessage())) {
			increaseStatisticValue(statisticTooLoud);
		}
	}
	
	private void incrementFWordIfNeeded(Message message) {
		if(message.getMessage().toLowerCase().contains(fWord)) {
			increaseStatisticValue(statisticTheWordWithFused);
		}
	}

	private void incrementLongPrivatesIfNeeded(PrivateMessage message) {
		if(message.getMessage().length() >= longMsgMinLength) {
			increaseStatisticValue(statisticLongPrivates);
		}
	}
	
	private void incrementShortPrivatesIfNeeded(PrivateMessage message) {
		if(message.getMessage().length() <= shortMsgMaxLength) {
			increaseStatisticValue(statisticShortPrivates);
		}
	}
	
	private void incrementSentPrivatesIfNeeded(PrivateMessage message) {
		if(message.getFromUserName().equals(user)) {
			increaseStatisticValue(statisticSentPrivates);
		}
	}
	
	private void incrementReceivedPrivatesIfNeeded(PrivateMessage message) {
		if(message.getToUserName().equals(user)) {
			increaseStatisticValue(statisticReceivedPrivates);
		}
	}

	private void increaseStatisticValue(String key) {
		Integer value = statistics.get(key);
		statistics.put(key, value + 1);	
	}
	
	private void addAllStatistic() {
		statistics.put(statisticSentPrivates, 0);
		statistics.put(statisticReceivedPrivates, 0);
		statistics.put(statisticPrivates, 0);
		statistics.put(statisticShortPrivates, 0);
		statistics.put(statisticLongPrivates, 0);
		statistics.put(statisticTheWordWithFused, 0);
		statistics.put(statisticRoomMsgs, 0);
		statistics.put(statisticTooLoud, 0);
	}

}
