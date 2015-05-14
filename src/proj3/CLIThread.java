package proj3;

import java.util.Scanner;

public class CLIThread extends Thread {

	
	
	public CLIThread() {
		
	}
	
	
	public void run() {
		ReadFromSystemIn();
	}
	
	public void ReadFromSystemIn() {
		Scanner sc = new Scanner(System.in);
        System.out.println("Printing the file passed in:");
        while(sc.hasNextLine()) {
        	
        	String input = sc.nextLine();
//        	System.out.println("Parsing " + input);
        	
        	ParseInput(input);
        }
	}
	
	public void ParseInput(String input) {
		if(input.substring(0, 1).equals("R")) {
			input += " ";
		}
		int len = input.length();
		String command = input.substring(0,input.indexOf(' ')); 
		String message = input.substring(input.indexOf(' ')+1); 
		System.out.println("c,m: " + command + ", " + message + "|||");
		
		
		
	}
	
}
