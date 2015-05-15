package proj3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class CommThread extends Thread{
	private boolean isRunning;
	//	private CLIThread parentThread;
	private ServerSocket serverSocket;
	private Socket socket;
	private ArrayList<Integer> quorum;
	private LinkedList<LockRequest> requests = new LinkedList<LockRequest>();
	private SiteLocks locks = new SiteLocks();
	int RECV_PORT_NO = 3000;
	public InetAddress privateIP;

	public CommThread(int siteNum, ArrayList<Integer> quorum, String ip){
		this.isRunning = true;
		this.quorum = quorum;
		try{
			privateIP = InetAddress.getByName(ip);
		}
		catch(UnknownHostException e){
			
		}
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

				boolean close = ParseCommand(socketIn.nextLine(), socketOut, socket);

				socketOut.flush();
				socketIn.close();
				socketOut.close();
				if(close){
					socket.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean ParseCommand(String cmd, PrintWriter writer, Socket socket) throws IOException{
		int site;
		if(cmd.substring(0, 7) == "RELEASE"){
			site = Integer.valueOf(cmd.substring(8, 9));
			locks.setLock(site - 1, SiteLocks.UNLOCKED);
			if(!requests.isEmpty()){
				LockRequest r = requests.element();
				if(r.getLock() == SiteLocks.READ){
					for(int i : quorum){
						if(locks.getLock(i) == SiteLocks.WRITE){
							return true;
						}
					}
					locks.setLock(r.getSite() - 1, SiteLocks.READ);
					PrintWriter writer2 = new PrintWriter(r.getSocket().getOutputStream(), true);
					writer2.println("YES READ");
					writer2.close();
					r.getSocket().close();
					requests.removeFirst();
				}
				else if(r.getLock() == SiteLocks.WRITE){
					for(int i : quorum){
						if(locks.getLock(i) > SiteLocks.UNLOCKED){
							return true;
						}
					}
					locks.setLock(site - 1, SiteLocks.WRITE);
					PrintWriter writer2 = new PrintWriter(r.getSocket().getOutputStream(), true);
					writer2.println("YES WRITE");
					writer2.close();
					r.getSocket().close();
					requests.removeFirst();
				}
			}
		}
		else if(cmd.substring(0, 9) == "READ LOCK"){
			site = Integer.valueOf(cmd.substring(11, 12));
			for(int i : quorum){
				if(locks.getLock(i) == SiteLocks.WRITE){
					requests.add(new LockRequest(SiteLocks.READ, site, socket));
					return false;
				}
			}
			locks.setLock(site - 1, SiteLocks.READ);
			writer.println("YES READ");
		}
		else if(cmd.substring(0, 10) == "WRITE LOCK"){
			site = Integer.valueOf(cmd.substring(12, 13));
			for(int i : quorum){
				if(locks.getLock(i) > SiteLocks.UNLOCKED){
					requests.add(new LockRequest(SiteLocks.WRITE, site, socket));
					return false;
				}
			}
			locks.setLock(site - 1, SiteLocks.WRITE);
			writer.println("YES WRITE");
		}
		return true;
	}
}
