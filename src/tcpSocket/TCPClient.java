package tcpSocket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
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
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		sentence = inFromUser.readLine();
		while (sentence.toLowerCase().compareTo("exit") != 0) {
			outToServer.writeBytes(sentence + '\n');
//			//Print empty space between user inputs
//			System.out.println();

			matchingWordsString = inFromServer.readLine();

			System.out.println("FROM SERVER: " + matchingWordsString);
			sentence = inFromUser.readLine();
		}

		clientSocket.close();
	}
}
