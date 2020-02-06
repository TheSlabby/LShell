package com.github.theslabby.lshell.common;

import java.io.IOException;
import java.net.*;

public class ServerSocket extends Thread{
	
	java.net.ServerSocket socket;
	int port;
	
	public ServerSocket(int port){
		this.port = port;
	}
	
	public void run() {
		System.out.println("Server Thread now listening!");
		
		try {
			socket = new java.net.ServerSocket(port);
		} catch (IOException e1) {
			System.out.println("Error starting server: "+port+" port most likely is currently in use!");
			System.exit(0);
		}
		
		while(socket != null) {
			try {
				
				Socket client = socket.accept();
				
				ClientHandler clientHandler = new ClientHandler(client);
				clientHandler.start();
				
				
			} catch (IOException e) {
				System.out.println("Failed to start new client handler!");
			}
		}
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("failed to close socket!");
		}
	}
}
