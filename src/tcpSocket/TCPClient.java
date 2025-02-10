package tcpSocket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {

	public static void main(String argv[]) throws Exception {
		String sentence;
		String matchingWordsString;

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		Socket clientSocket = new Socket("127.0.0.1", 6789);
		System.out.println("Client successfully established TCP connection.\n"
				+ "Client(local) end of the connection uses port " 
				+ clientSocket.getLocalPort() 
				+ " and server(remote) end of the connection uses port "
				+ clientSocket.getPort());

		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream()); 	//Better used to receive UTF stream 

		System.out.println("\nType a Prefix to Find Matching Words from Server: ");
		sentence = inFromUser.readLine();
		while (sentence.toLowerCase().compareTo("exit") != 0) {
			outToServer.writeBytes(sentence + '\n');
			matchingWordsString = inFromServer.readUTF();
			System.out.println("FROM SERVER: " + matchingWordsString + '\n');
			System.out.println("Type a Prefix to Find Matching Words from Server: ");
			sentence = inFromUser.readLine();
		}

		clientSocket.close();
	}
}
