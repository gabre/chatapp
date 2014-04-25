package test;

import static org.junit.Assert.*;

import java.util.Date;

import model.Model;
import model.PrivateMessage;
import model.RoomMessage;
import model.UserSettings;

import org.junit.Test;

public class ModelTest {

	private static final String userName = "userNAME";

	@Test
	public void testStartConnectionInModel() {
		Model model = new Model();
		model.startConnection(userName);
		UserSettings userSettings = model.getUserInfo();
		assertEquals(userSettings.getUserName(), userName);
	}

	@Test
	public void testChatMessageHandling() {
		Model model = new Model();
		model.startConnection(userName);
		model.getMessages().add(new PrivateMessage("1", "2", new Date(), "4"));
		model.getMessages().add(new PrivateMessage("2", "22", new Date(), "44"));
		RoomMessage myRoomMsg = new RoomMessage("3", new Date(), "33", "34");
		model.getMessages().add(myRoomMsg);
		PrivateMessage myPrivateMsg = new PrivateMessage("3", "45", new Date(), "34");
		model.getMessages().add(myPrivateMsg);
		
		model.deleteChatMessage(myRoomMsg);
		assertFalse(model.getMessages().contains(myRoomMsg));
		
		model.deleteChatMessage(myPrivateMsg);
		assertFalse(model.getMessages().contains(myPrivateMsg));
		
		model.deleteChatMessages();
		assertTrue(model.getMessages().isEmpty());
		
	}
	
	@Test
	public void testCalculatingStatisticsGeneratesResults() {
		Model model = new Model();
		model.startConnection(userName);
		model.getMessages().add(new PrivateMessage("1", userName, new Date(), "4"));
		model.getMessages().add(new PrivateMessage("1", userName, new Date(), "44"));
		
		model.calculateStatistics();
		
		assertFalse(model.getStatistics().isEmpty());
		assertFalse(model.getBestFriends().isEmpty());
	}

}
