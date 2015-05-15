package proj3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class LogThread extends Thread{
	private boolean isRunning;
	private ArrayList<String> log = new ArrayList<String>();
	private String[] lock = new String[5];
	int LOG_RECV_PORT = 3050;
	private ServerSocket serverSocket;
	private Socket socket;
	private InetAddress privateIP;

	public LogThread(String addr){
		isRunning = true;
		try{
			privateIP = InetAddress.getByName(addr);
		}
		catch(UnknownHostException e){
			
		}
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
		serverSocket = new ServerSocket(LOG_RECV_PORT);
		while(isRunning){
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
	
	private void ParseCommand(String input, PrintWriter writer){
		String command = input.substring(0, input.indexOf(" "));
		if(command.equals("SEND")){
			String info = input.substring(input.indexOf(" ") + 1);
			lock[0] = info.substring(0, 1);
			lock[1] = info.substring(1, 2);
			lock[2] = info.substring(2, 3);
			lock[3] = "shared";
			lock[4] = info.substring(3, 4);
			System.out.println("Site" + lock[4] + " " + lock[3] + " lock quorum " + lock[0] + ", " + lock[1] + " and " + lock[2]);
			String temp = "";
			for(String s : log){
				temp += s;
				temp += ", ";
			}
			writer.println(temp);
		}
		else if(command.equals("APPEND")){
			String info = input.substring(input.indexOf(" ") + 1);
			lock[0] = info.substring(0, 1);
			lock[1] = info.substring(1, 2);
			lock[2] = info.substring(2, 3);
			lock[3] = "exclusive";
			lock[4] = info.substring(3, 4);
			System.out.println("Site" + lock[4] + " " + lock[3] + " lock quorum " + lock[0] + ", " + lock[1] + " and " + lock[2]);
			String message = info.substring(4);
			log.add(message);
			writer.println("ACK");
		}
		else if(command.equals("RELEASE")){
			System.out.println("Site" + lock[4] + " releases " + lock[3] + " lock quorum " + lock[0] + ", " + lock[1] + " and " + lock[2]);
			writer.println("ACK");
		}
	}
}
