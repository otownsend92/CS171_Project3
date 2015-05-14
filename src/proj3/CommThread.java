package proj3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class CommThread extends Thread{
	private boolean isRunning;
//	private CLIThread parentThread;
	private ServerSocket serverSocket;
	private Socket socket;
	private ArrayList<Integer> quorum;
	private SiteLocks locks = new SiteLocks();
	int RECV_PORT_NO = 3000;

	public CommThread(int siteNum, ArrayList<Integer> quorum){
		this.isRunning = true;
		this.quorum = quorum;
		// parentThread = et;
	}

	public void run(){
		// Start listening
		try {
			listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void listen() throws IOException{
		serverSocket = new ServerSocket(RECV_PORT_NO);
		while (isRunning) {
			try {
				socket = serverSocket.accept();
				Scanner socketIn 		= new Scanner(socket.getInputStream());
				PrintWriter socketOut 	= new PrintWriter(socket.getOutputStream(), true);
				
				ParseCommand(socketIn.nextLine(), socketOut);
				
				socketOut.flush();
				socketIn.close();
				socketOut.close();
				socket.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void ParseCommand(String cmd, PrintWriter writer){
		int site;
		if(cmd.substring(0, 7) == "RELEASE"){
			site = Integer.valueOf(cmd.substring(8, 9));
			locks.setLock(site - 1, SiteLocks.UNLOCKED);
		}
		else if(cmd.substring(0, 9) == "READ LOCK"){
			for(int i : quorum){
				if(locks.getLock(i) == SiteLocks.WRITE){
					return;
				}
			}
			site = Integer.valueOf(cmd.substring(11, 12));
			locks.setLock(site - 1, SiteLocks.READ);
			writer.println("YES READ");
		}
		else if(cmd.substring(0, 10) == "WRITE LOCK"){
			for(int i : quorum){
				if(locks.getLock(i) > SiteLocks.UNLOCKED){
					return;
				}
			}
			site = Integer.valueOf(cmd.substring(12, 13));
			locks.setLock(site - 1, SiteLocks.WRITE);
			writer.println("YES WRITE");
		}
	}
}
