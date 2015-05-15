package proj3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class CLIThread extends Thread {
	
	int RECV_PORT_NO = 3000;
	int LOG_RECV_PORT = 3050;
	ArrayList<Integer> quorum;
	private int myID;
	public static String[] IpAddrs = new String[5];
	public static String logSiteIP = new String();

	public CLIThread(int id, ArrayList<Integer> quorum) {
		this.myID = id;
		this.quorum = quorum;
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
		sc.close();
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
			if (message.length() > 140){
				message = message.substring(0, 140);
			}
			System.out.println("Appending " + message + "$");
			AppendToLog(message);
		}
	}
	
	
	/*
	 * Main method that initates read process from log.
	 * Initiates quorum process, reads from log, and releases.
	 */
	public void ReadFromLog() throws InterruptedException, UnknownHostException, IOException {
		/*
		 * Contact other sites to get quorum for read lock
		 */
		System.out.println("ReadFromLog");
		boolean hasLock = ObtainReadLock();
		while (!hasLock) {
			Thread.sleep(500);	// Sleep half a second then try again.
			hasLock = ObtainReadLock();
		}
		
		System.out.println("Quorum");
		/*
		 * Once quorum is achieved, contact log site with 
		 * quorum info. Receive log from log site and print
		 * it to standard output.
		 */
		if (hasLock) {
			String currentLog = RequestReadFromLog();
			System.out.println("Current log:\n"+currentLog);
		}
		
		System.out.println("Releasing");
		/*
		 * After printing, release lock:
		 */
		boolean released = ReleaseLock();
		if (!released) {
			System.out.println("Couldn't release read lock.");
		}
	}
	
	public void AppendToLog(String message) throws InterruptedException, UnknownHostException, IOException {
		boolean hasLock = ObtainWriteLock();
		while(!hasLock){
			Thread.sleep(500);
			hasLock = ObtainWriteLock();
		}
		
		if(hasLock){
			System.out.println("We have a lock!");
			RequestAppendToLog(message);
		}
		
		boolean released = ReleaseLock();
		if (!released) {
			System.out.println("Couldn't release read lock.");
		}
	}
	
	
	/*
	 * 	1.	Site sends release message to log process.
	 *	2.	Log process prints release message in standard output	 
	 * 	3.	Log process replies back to site process with an ack
	 * 		message.
	 * 	4.	Upon receiving ack from log, site sends release message
	 * 		to all sites of quorum.
	 */
	public boolean ReleaseLock() throws UnknownHostException, IOException {
		String rel 				= null;
		Socket socket 			= new Socket(logSiteIP, LOG_RECV_PORT);
		Scanner socketIn 		= new Scanner(socket.getInputStream());
		PrintWriter socketOut 	= new PrintWriter(socket.getOutputStream(), true);
		
		socketOut.println("RELEASE ");
		
		if (!socketIn.hasNext()) {
			; // Do nothing.
		}
		else {
			rel = socketIn.nextLine();
			if(!rel.equals("ACK")) {
				socketIn.close();
				socket.close();
				return false;
			}
		}
		
		/*
		 * Close everything.
		 */
		socketOut.flush();
		socketIn.close();
		socketOut.close();
		socket.close();
		
		/*
		 * Send RELEASE to other sites in your quorum.
		 */
		for(int i : quorum) {
			Socket siteSock 			= new Socket(IpAddrs[i - 1], RECV_PORT_NO);
			PrintWriter socketOutSite 	= new PrintWriter(socket.getOutputStream(), true);
			
			socketOut.println("RELEASE " + myID);
			
			// Close.
			socketOutSite.flush();
			socketOutSite.close();
			siteSock.close();
		}
		
		return true;
	}
	
	
	/*
	 * Request a copy of the log from log site.
	 */
	public String RequestReadFromLog() throws UnknownHostException, IOException {
		String log 				= null;
		Socket socket 			= new Socket(logSiteIP, LOG_RECV_PORT);
		Scanner socketIn 		= new Scanner(socket.getInputStream());
		PrintWriter socketOut 	= new PrintWriter(socket.getOutputStream(), true);
		
		socketOut.println("SEND " + quorum.get(0) + quorum.get(1) + quorum.get(2) + myID); 
		// Tell log you want log, include your site ID plus the two sites
		// that gave you permission (your quorum).
		
		if (!socketIn.hasNext()) {
			; // Do nothing.
		}
		else {
			log = socketIn.nextLine();
		}
		
		/*
		 * Close everything.
		 */
		socketOut.flush();
		socketIn.close();
		socketOut.close();
		socket.close();
		
		return log;
	}
	
	public void RequestAppendToLog(String message) throws UnknownHostException, IOException {
		Socket socket 			= new Socket(logSiteIP, LOG_RECV_PORT);
		PrintWriter socketOut 	= new PrintWriter(socket.getOutputStream(), true);
		
		socketOut.println("APPEND " + quorum.get(0) + quorum.get(1) + quorum.get(2) + myID + message); 
		
		socketOut.flush();
		socketOut.close();
		socket.close();
	}
	
	
	/*
	 * Contact three other sites to get permission for read lock.
	 */
	public boolean ObtainReadLock() throws UnknownHostException, IOException {
		int count = 0; // want this to equal 3 for 3 sites agreeing
		
		/*
		 * Open socket connection with three other sites.
		 * For now, our quorum is just ourselves plus the two
		 * random sites.
		 */
		for(int i : quorum) {
			String answer 			= null;
			Socket socket 			= new Socket(IpAddrs[i - 1], RECV_PORT_NO);
			Scanner socketIn 		= new Scanner(socket.getInputStream());
			PrintWriter socketOut 	= new PrintWriter(socket.getOutputStream(), true);
			
			socketOut.println("READ LOCK " + myID); 	// Say you want a read lock
														// and include your site ID
			while (!socketIn.hasNext()) {
				; // Do nothing.
			}
			
			System.out.println("Got Something");
			
			answer = socketIn.nextLine();
			if (answer.equals("YES READ")) {
				count++;
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
	
	public boolean ObtainWriteLock() throws UnknownHostException, IOException {
		int count = 0; // want this to equal 3 for 3 sites agreeing
		
		/*
		 * Open socket connection with three other sites.
		 * For now, our quorum is just ourselves plus the two
		 * random sites.
		 */
		for(int i : quorum) {
			String answer 			= null;
			Socket socket 			= new Socket(IpAddrs[i - 1], RECV_PORT_NO);
			Scanner socketIn 		= new Scanner(socket.getInputStream());
			PrintWriter socketOut 	= new PrintWriter(socket.getOutputStream(), true);
			
			socketOut.println("WRITE LOCK " + myID); 	// Say you want a read lock
														// and include your site ID
			while (!socketIn.hasNext()) {
				; // Do nothing.
			}
			
			answer = socketIn.nextLine();
			if (answer.equals("YES WRITE")) {
				count++;
			}
			
			
			/*
			 * Close everything.
			 */
			socketOut.flush();
			socketIn.close();
			socketOut.close();
			socket.close();
		}
		
		if (count != 3){
			System.out.println("No write lock.");
			return false;
		}
		else{
			System.out.println("Write lock.");
			return true;
		}
		
	}
	

}
