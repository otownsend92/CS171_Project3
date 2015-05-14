package proj3;

import java.util.Scanner;

public class CLIThread extends Thread {

	public CLIThread() {

	}

	public void run() {
		try {
			ReadFromSystemIn();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ReadFromSystemIn() throws InterruptedException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Printing the file passed in:");
		while (sc.hasNextLine()) {
			String input = sc.nextLine();
			ParseInput(input);
		}
	}

	public void ParseInput(String input) throws InterruptedException {
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
	
	public void ReadFromLog() throws InterruptedException {
		/*
		 * Contact other sites to get quorum for read lock
		 */
		boolean hasLock = ObtainReadLock();
		while (!hasLock) {
			Thread.sleep(500);	// Sleep half a second then try again.
			hasLock = ObtainReadLock();
		}
		
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
	
	
	/*
	 * Contact three other sites to get permission for read lock.
	 */
	public boolean ObtainReadLock() {
		/*
		 * Open socket connection with three other sites.
		 * For now, our quorum is just ourselves plus the two
		 * sites with IDs myID+1 and myID+2
		 */
		
		// Ask for read permission.
		// If all 3 reply "YES READ", return true.
		// If one of 3 replies "NO READ", return false
		
		return true;	// Remove later.
	}
	
	public boolean ReleaseLock() {
		
		return true;	// Remove later.
	}

}
