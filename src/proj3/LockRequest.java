package proj3;

import java.net.Socket;

public class LockRequest {
	private int lockType;
	private int site;
	private Socket socket;
	
	public LockRequest(int i, int site, Socket s){
		lockType = i;
		this.site = site;
		socket = s;
	}
	
	public int getLock(){
		return lockType;
	}
	
	public int getSite(){
		return site;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
}
