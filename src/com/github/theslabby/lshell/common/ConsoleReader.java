package com.github.theslabby.lshell.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader extends Thread {
	
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	public void run() {
		while (true) {
			try {
				String line = reader.readLine();
				if (line != null) {
					System.out.println(line);
				}else {
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			
		}
		System.out.println("Stopped reading from console!");
	}
}
