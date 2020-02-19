package com.github.theslabby.lshell;

import com.github.theslabby.lshell.common.ConsoleReader;
import com.github.theslabby.lshell.common.ServerSocket;

public class ServerMain {

	public static final String OS = System.getProperty("os.name").toLowerCase();
	public static int port = 4545;
	
	public static void main(String[] args) {
		
		  try {
		        port = Integer.parseInt(args[0]);
		    } catch (Exception e) {
		    	System.out.println("Didn't find port, using 4545");
		    }
		
		//start listening
		ServerSocket serverThread = new ServerSocket(port);
		serverThread.start();
		
		//start reading for console commands
		ConsoleReader consoleReader = new ConsoleReader();
		consoleReader.start();
	}

}
