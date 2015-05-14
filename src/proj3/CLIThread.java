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
		while (sc.hasNextLine()) {
			String input = sc.nextLine();
			ParseInput(input);
		}
	}

	public void ParseInput(String input) {
		input += " ";

		String command = input.substring(0, input.indexOf(' '));
		String message = input.substring(input.indexOf(' ') + 1);

		if (command.equals("Read")) {
			System.out.println("Reading!!");
			ReadFromLog();
		}
		
		else if (command.equals("Append")) {
			if (message.length() > 0
					&& message.charAt(message.length() - 1) == ' ') {
				message = message.substring(0, message.length() - 1);
			}
			System.out.println("Appending " + message + "$");
		}
	}
	
	public void ReadFromLog() {
		/*
		 * Contact other sites to get quorum for read lock
		 */
		boolean hasLock = ObtainReadLock();
		
		/*
		 * Once quorum is achieved, contact log site with 
		 * quorum info. Receive log from log site and print
		 * it to standard output.
		 */
		if (hasLock) {
			
		}
		
		/*
		 * After printing, release lock:
		 * 	1.	Site sends release message to log process.
		 * 	2.	Log process prints release message in standard output
		 * 	3.	Log process replies back to site process with an ack
		 * 		message.
		 * 	4.	Upon receiving ack from log, site sends release message
		 * 		to all sites of quorum.
		 */
		boolean released = ReleaseLock();
		
	}
	
	public boolean ObtainReadLock() {
		
		return true;	// Remove later.
	}
	
	public boolean ReleaseLock() {
		
		return true;	// Remove later.
	}

}
