import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {

	public static void main(String[] args) throws Exception {
		ServerSocket sock = new ServerSocket(12345);
		sock.setReuseAddress(true);
		Socket client = sock.accept();
		BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
		final BufferedWriter output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		
		output.write("USER+ sanyika"); output.newLine();
		output.write("USER+ pistike"); output.newLine();
		output.write("CHAN+ #husoskenyer"); output.newLine();
		output.write("CHAN+ #negyzetgyok"); output.newLine();
		output.flush();
		
		while (true) {
			String msg = input.readLine();
			if (msg == null || msg.equals("QUIT")) break;
			System.out.println(msg);
			output.write("MSG #husoskenyer sanyika üzenetet küldött a kliens");
			output.newLine();
			output.write("PRIVMSG pistike szia én vagyok a pistike :3");
			output.newLine();
			output.flush();
		}
		client.close();
		sock.close();
	}

}
