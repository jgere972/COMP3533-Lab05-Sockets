import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	public static void main(String argv[]) throws Exception {
		String clientSentence;
		String capitalizedSentence;

		ServerSocket welcomeSocket = new ServerSocket(6789);

		while (true) {

			System.out.println("ServerSocket created, blocking on accept and waiting for incoming requests!");
			Socket connectionSocket = welcomeSocket.accept();
			
			System.out.println("ServerSocket accepted incoming request from: " + connectionSocket.getPort());
			
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			System.out.println("Accepted TCP connection from" 
					+ connectionSocket.getInetAddress() 
					+ ":" + connectionSocket.getPort());
			try {
				while (true) {
					clientSentence = inFromClient.readLine();

					capitalizedSentence = clientSentence.toUpperCase() + '\n';

					outToClient.writeBytes(capitalizedSentence);
				}
			} catch (Exception e) {
				// TODO: handle exception, if client closed connection, print:
				System.out.println("Client closed connection.");
			}
		}
	}
}
