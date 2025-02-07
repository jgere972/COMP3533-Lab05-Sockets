# COMP3533-Lab06: Socket-Programming-Demo

## Goal: 
This is a simple Demo of socket programming.

## Description: 
This is meant to be an implementation of a local server and a client program using TCP sockets in Java. The server maintains a word repository (words.txt). This word file contains a list of 25143 words (a single word per line). At the prompt, the user can type any single word, such as:

*“yel”*

Then, the message will be sent over the network to the server.  The server will find all the words that share the same prefix with the client’s word, and sent them back to the client as a comma-separated string. For example, in response to the word “yel” sent from the client, the server should send back the following list of words.

        “yellow, yellowish, Yellowknife, Yellowstone”
 
Upon receiving the list of words that share the same prefix with the original word, the client should display the list to the client's terminal, with a preceding message as follows:

        “The following words start with yel: yellow, yellowish, Yellowknife, Yellowstone”

The user can request the prefix match for other words over and over until a special character or phrase such as “exit” is entered by the user to stops the client program (you can choose a different kill word or character, just state it clearly in your documentation).  The server will keep running forever, listening for incoming connection requests from other clients.

## How it works: 

**Steps**

1. Run local server side program
2. Run the client side program on the same machine or on a different machine to communicate with the server.


