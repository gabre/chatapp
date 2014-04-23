import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {

	public static void main(String[] args) throws Exception {
		ServerSocket sock = new ServerSocket(12346);
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
			if (msg == null) return;
			System.out.println(msg);
			output.write("MSG #husoskenyer sanyika üzenetet küldött a kliens");
			output.newLine();
			output.flush();
		}
	}

}
