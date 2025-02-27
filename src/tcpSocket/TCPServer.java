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
//		List<String> captializedWords = new ArrayList<String>(); //OPTIONAL - list meant to capture capitalized matching words
		String matchingWordsString;
		BufferedReader inFile = null;
		File file = new File("bin/src/tcpSocket/words.txt");
		
		ServerSocket welcomeSocket = new ServerSocket(6789);

		while (true) {
			System.out.println("\nServerSocket created, blocking on accept and waiting for incoming requests!");
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
						// Read q word form the the files and Find the words that match the pre-fix provided by Client
						while((word = inFile.readLine()) != null) {
							/**
							 * Validation checks: Skipping empty Strings and words that are shorter than Client provided prefixes
							 **/
							//1. Skip empty word Strings
							if(word.isEmpty()) {
								continue; 
							}
							//2. Skip word if the prefix is bigger (more characters) than the word itself
							if (clientPrefix.length() > word.length()){
								continue;
							}
							String minimizedClientPrefix = clientPrefix.toLowerCase();
							boolean match = true;
							for(int i = 0; i < clientPrefix.length(); i++) {
								//Minimize each word processed to fairly compare minimized Client provided prefix with that lower case word
								String minimizedWord = word.toLowerCase();
									// match = false, if characters between prefix and word do not match.
									if(minimizedClientPrefix.charAt(i) != minimizedWord.charAt(i)) {
										match = false;
										break;
									}
								}
								if(match == true) {
									listMatchingWords.add(word);
							}
						}
						//Captialize the first letter of each listMatchingWords (OPTIONAL!!)
//						for (String w : listMatchingWords) {
//							captializedWords.add(w.substring(0, 1).toUpperCase() + w.substring(1).toLowerCase());
//						}
						// Join all Strings together with "," as a delimiter
						matchingWordsString = String.join(", ", listMatchingWords);
						// Write the String of words back to the client (Best to use writeUTF for Strings)
						outToClient.writeUTF(matchingWordsString);
						// Flushes this data output stream. This forces any buffered output bytes to be written out to the stream.
						outToClient.flush();
						// Clear the List of matching words and the list of capitalized words
						listMatchingWords.clear();
//						captializedWords.clear();
					} catch(FileNotFoundException e) {	//Handle word.txt not found exception
						System.err.println("File not found: " + e.getMessage());
					} catch (IOException e) { //Handle issue with IO access
						System.err.println("I/O error: " + e.getMessage());
						// Close File
						inFile.close();
					}
				}
			} catch (Exception e) { //Handle general exceptions
				// Close Socket (Not Closing ServerSocket since It needs to keep listening for requests for new Sockets)
				connectionSocket.close();
				// Closes this output stream and releases any system resources associated with the stream
				inFromClient.close();
				outToClient.close(); 					
				System.out.println("Client closed connection.");
			}
		}
	}
}
