package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import model.Connection;

import org.junit.Test;

public class ConnectionTest {

	private static final int port = 12355;
	private static final String address = "localhost";
	private static final String name = "my_name";
	
	private ServerSocket sock;
	

	@Test
	public void testConnectAndClose() throws UnknownHostException, IOException {
		try {
			sock = new ServerSocket(port);
			sock.setReuseAddress(true);
			sock.accept();
		} catch(IOException e) {
			fail("Problem with test: IOException when trying to create server, " + e.getMessage());
		}
		Connection connection = new Connection(address, port, name);
		connection.close();
		sock.close();
	}
	
	@Test(expected=UnknownHostException.class)
	public void testNoServerAvailable() throws IOException {
		new Connection(address, port, name);
	}

}
