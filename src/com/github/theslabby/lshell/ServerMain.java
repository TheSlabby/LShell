package com.github.theslabby.lshell;

import com.github.theslabby.lshell.common.ConsoleReader;
import com.github.theslabby.lshell.common.ServerSocket;

public class ServerMain {

	public static void main(String[] args) {
		
		//start listening
		ServerSocket serverThread = new ServerSocket(4545);
		serverThread.start();
		
		//start reading for console commands
		ConsoleReader consoleReader = new ConsoleReader();
		consoleReader.start();
	}

}
