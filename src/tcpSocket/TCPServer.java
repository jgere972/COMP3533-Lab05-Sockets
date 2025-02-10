package tcpSocket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class TCPServer {
	public static void main(String argv[]) throws Exception{
		String clientPrefix;
		String word;
		List<String> listMatchingWords = new ArrayList<String>();
		List<String> captializedWords = new ArrayList<String>();
		String matchingWordsString;
		BufferedReader inFile = null;
		File file = new File("bin/src/tcpSocket/words.txt");
		
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
					try {
						// Read pre-fix provided by Client host
						clientPrefix = inFromClient.readLine();
						// Read file into memory (efficient read using BufferedReader)
						inFile = new BufferedReader(new FileReader(file));
						// Read a word from words.txt
						word = inFile.readLine();
						// Find words in the words.txt file matching the pre-fix provided by Client
						while((word = inFile.readLine()) != null) {
							//Validation checks
							
							//1. Skip empty word Strings
							if(word.isEmpty()) {
								continue; 
							}
							//2. Skip word if the prefix is bigger (more characters) than the word itself
							if (clientPrefix.length() > word.length()){
								continue;
							}
							
							boolean match = true;
							for(int i = 0; i < clientPrefix.length(); i++) {
									// match = false if word is shorter in length to the prefix char index 
									// OR if characters between prefix and word do not match.
									if(clientPrefix.charAt(i) != word.charAt(i)) {
										match = false;
										break;
									}
								}
								if(match == true) {
									listMatchingWords.add(word);
							}
						}
						//Captialize the first letter of each listMatchingWords
						for (String w : listMatchingWords) {
							captializedWords.add(w.substring(0, 1).toUpperCase() + w.substring(1).toLowerCase());
						}
						// Join all Strings together with "," as a delimiter
						matchingWordsString = String.join(", ", captializedWords);
						// Write the String of words back to the client (Best to use writeUTF for Strings)
						outToClient.writeUTF(matchingWordsString);
						// Flushes this data output stream. This forces any buffered output bytes to be written out to the stream.
						outToClient.flush();
						// Clear the List of matching words and the list of capitalized words
						listMatchingWords.clear();
						captializedWords.clear();
					} catch(FileNotFoundException e) {	//Handle word.txt not found exception
						System.err.println("File not found: " + e.getMessage());
					} catch (IOException e) { //Handle issue with IO access
						System.err.println("I/O error: " + e.getMessage());
						// Close File
						inFile.close();
					}
				}
			} catch (Exception e) { //Handle general exceptions
				// Close Socket
				connectionSocket.close();
				// Closes this output stream and releases any system resources associated with the stream
				outToClient.close(); 					
				System.out.println("Client closed connection.");
				System.out.println("Exception: " + e.getMessage());
			}
		}
	}
}
