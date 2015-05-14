package proj3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CLIThread extends Thread {
	
	int RECV_PORT_NO = 3000;
	
	private int myID;
	private String[] IpAddrs = {
			"xxx.xxx.xxx.xxx",
			"xxx.xxx.xxx.xxx",
			"xxx.xxx.xxx.xxx",
			"xxx.xxx.xxx.xxx",
			"xxx.xxx.xxx.xxx",
	};

	public CLIThread(int id) {
		this.myID = id;
	}

	public void run() {
		try {
			ReadFromSystemIn();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ReadFromSystemIn() throws InterruptedException, UnknownHostException, IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Printing the file passed in:");
		while (sc.hasNextLine()) {
			String input = sc.nextLine();
			ParseInput(input);
		}
	}

	public void ParseInput(String input) throws InterruptedException, UnknownHostException, IOException {
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
	
	public void ReadFromLog() throws InterruptedException, UnknownHostException, IOException {
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
	public boolean ObtainReadLock() throws UnknownHostException, IOException {
		int count = 0; // want this to equal 3 for 3 sites agreeing
		
		/*
		 * TODO: FIRST, mark your own lock object as 'reading'
		 */
		count++;	// I give myself permission.
		
		
		/*
		 * Open socket connection with three other sites.
		 * For now, our quorum is just ourselves plus the two
		 * sites with IDs myID+1 and myID+2
		 */
		int i = 0;
		while (i < 3) {
			String answer 			= null;
			Socket socket 			= new Socket(IpAddrs[myID+i], RECV_PORT_NO+1);
			Scanner socketIn 		= new Scanner(socket.getInputStream());
			PrintWriter socketOut 	= new PrintWriter(socket.getOutputStream(), true);
			
			socketOut.println("READ LOCK " + myID); 	// Say you want a read lock
														// and include your site ID
			if (!socketIn.hasNext()) {
				; // Do nothing.
			}
			else {
				answer = socketIn.nextLine();
				if (answer.equals("YES READ")) {
					count++;
				}
			}
			
			/*
			 * Close everything.
			 */
			socketOut.flush();
			socketIn.close();
			socketOut.close();
			socket.close();
		}
		
		if (count != 3)
			return false;
		else
			return true;
		
	}
	
	public boolean ReleaseLock() {
		
		return true;	// Remove later.
	}

}
