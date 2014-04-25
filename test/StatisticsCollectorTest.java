package test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import model.Model;
import model.PrivateMessage;
import model.RoomMessage;
import model.Statistics;
import model.StatisticsCollector;
import model.UserSettings;

import org.junit.Test;

public class StatisticsCollectorTest {

	private static final String userName = "userNAME";
	private String anotherUser = "to1";
	private PrivateMessage privateMessage = new PrivateMessage(userName, anotherUser, new Date(), "msg");
	private RoomMessage roomMessage = new RoomMessage(userName, new Date(), "room", "msg");

	@Test
	public void testCollectorMessageCnt() {
		StatisticsCollector statisticsCollector = new StatisticsCollector(userName);
		statisticsCollector.visit(privateMessage);
		statisticsCollector.visit(roomMessage);
		Statistics stat = getElem(StatisticsCollector.statisticSentPrivates, statisticsCollector.getStatistics());
		assertEquals("1", stat.getValue());
		Statistics roomStat = getElem(StatisticsCollector.statisticRoomMsgs, statisticsCollector.getStatistics());
		assertEquals("1", stat.getValue());
	}
	
	@Test
	public void testCollectorPrivates() {
		StatisticsCollector statisticsCollector = new StatisticsCollector(userName);

		PrivateMessage privateMessage2 = new PrivateMessage(userName, "to", new Date(), "anything");
		PrivateMessage privateMessage3 = new PrivateMessage("somebody", userName, new Date(), "something");
		
		statisticsCollector.visit(privateMessage);
		statisticsCollector.visit(privateMessage2);
		statisticsCollector.visit(privateMessage3);
		
		Statistics stat = getElem(StatisticsCollector.statisticSentPrivates, statisticsCollector.getStatistics());
		assertEquals("2", stat.getValue());
		Statistics allPrivateStat = getElem(StatisticsCollector.statisticPrivates, statisticsCollector.getStatistics());
		assertEquals("3", allPrivateStat.getValue());
		Statistics receivedPrivateStat = getElem(StatisticsCollector.statisticReceivedPrivates, statisticsCollector.getStatistics());
		assertEquals("1", receivedPrivateStat.getValue());
	}
	
	@Test
	public void testCollectorLongAndShortMsgs() {
		StatisticsCollector statisticsCollector = new StatisticsCollector(userName);
		
		final char[] array = new char[101];
	    Arrays.fill(array, 'a');
	    String msg = new String(array);
		PrivateMessage privateMessage2 = new PrivateMessage(userName, "to", new Date(), msg);
		PrivateMessage privateMessage3 = new PrivateMessage(userName, "to", new Date(), "anything");
		
		statisticsCollector.visit(privateMessage2);
		statisticsCollector.visit(privateMessage3);
		
		Statistics longStat = getElem(StatisticsCollector.statisticLongPrivates, statisticsCollector.getStatistics());
		assertEquals("1", longStat.getValue());
		Statistics shortStat = getElem(StatisticsCollector.statisticShortPrivates, statisticsCollector.getStatistics());
		assertEquals("1", shortStat.getValue());
	}
	
	@Test
	public void testCollectorRoomMsgs() {
		StatisticsCollector statisticsCollector = new StatisticsCollector(userName);
		
		RoomMessage roomMessage2 = new RoomMessage(userName, new Date(), "room", "something!");
		RoomMessage roomMessage3 = new RoomMessage(userName, new Date(), "room", "anything");
		
		statisticsCollector.visit(roomMessage);
		statisticsCollector.visit(roomMessage2);
		statisticsCollector.visit(roomMessage3);
		
		Statistics longStat = getElem(StatisticsCollector.statisticRoomMsgs, statisticsCollector.getStatistics());
		assertEquals("3", longStat.getValue());
	}
	
	@Test
	public void testCollectorTooLoudMessages() {
		StatisticsCollector statisticsCollector = new StatisticsCollector(userName);
		
		PrivateMessage tooLoud = new PrivateMessage(userName, "user222", new Date(), "LOUD!");
		
		statisticsCollector.visit(privateMessage);
		statisticsCollector.visit(tooLoud);
		
		Statistics longStat = getElem(StatisticsCollector.statisticTooLoud, statisticsCollector.getStatistics());
		assertEquals("1", longStat.getValue());
	}
	
	@Test
	public void testCollectorFWordMessages() {
		StatisticsCollector statisticsCollector = new StatisticsCollector(userName);
		
		PrivateMessage fWord = new PrivateMessage(userName, "user222", new Date(), "fuck!");
		
		statisticsCollector.visit(privateMessage);
		statisticsCollector.visit(fWord);
		
		Statistics longStat = getElem(StatisticsCollector.statisticTheWordWithFused, statisticsCollector.getStatistics());
		assertEquals("1", longStat.getValue());
	}
	
	@Test
	public void testCollectorBestFriends() {
		StatisticsCollector statisticsCollector = new StatisticsCollector(userName);
		String anotherUser2 = "anotherUser2";
		
		PrivateMessage privateMessage2 = new PrivateMessage(userName, anotherUser2, new Date(), "anything!");
		
		statisticsCollector.visit(privateMessage);
		statisticsCollector.visit(privateMessage2);
		
		List<Entry<String, Integer>> bestFriends = statisticsCollector.getBestFriends();
		
		assertEquals(2, bestFriends.size());
		assertEquals(Integer.valueOf(1), bestFriends.get(0).getValue());
		assertEquals(Integer.valueOf(1), bestFriends.get(1).getValue());
		
	}

	private Statistics getElem(String name, List<Statistics> statistics) {
		boolean found = false;
		int i = -1;
		while(i+1<statistics.size() && !found) {
			if(statistics.get(i+1).getName().equals(name)) {
				found = true;
			}
			++i;
		}
		return statistics.get(i);
	}
	
}
