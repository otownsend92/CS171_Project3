package proj3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

public class MainThread{
	private Thread cliThread = new Thread();
	private Thread commThread = new Thread();
	ArrayList<Integer> quorum = new ArrayList<Integer>();
	
	public MainThread(int id, String addr){
		// Constructor
		quorum.add(id);
		int i = 1;
		while(i < 3){
			int site = randInt(1,5);
			if(site == id){
				continue;
			}
			quorum.add(site);
			i++;
		}
		cliThread = new CLIThread(id, quorum);
		cliThread.start();
		commThread = new CommThread(id, quorum, addr);
		commThread.start();
	}
	
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	
	/*
	 * Firstline,siteid(1,2,3,4,5or6(forthelog))
	 * Lines 2-7 contain the public IP and the port number 
	 * of sites 1, 2, 3, 4, 5 and the log respectively.
	 * Line 8 contains the private IP and the port number of the current instance.
	 * Example, site 1 might have the following configuration file:
	 * 1
	 * 51.23.41.126 5352 
	 * 51.23.41.182 5352 
	 * 51.23.41.130 5352 
	 * 51.23.41.237 5352 
	 * 51.23.41.213 5352 
	 * 51.23.41.164 5352 
	 * 172.16.0.174 5352
	 */
	
	public static void main(String[] args) throws IOException{
		// Read from file and get side ID
		String path = "config.txt";
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		
		int myID 		= Integer.parseInt(br.readLine());
		String site1 	= br.readLine();
		String site2 	= br.readLine();
		String site3 	= br.readLine();
		String site4 	= br.readLine();
		String site5 	= br.readLine();
		String log 		= br.readLine();
		String privIP 	= br.readLine();
		
		site1 = site1.substring(0, site1.indexOf(' '));
		site2 = site2.substring(0, site2.indexOf(' '));
		site3 = site3.substring(0, site3.indexOf(' '));
		site4 = site4.substring(0, site4.indexOf(' '));
		site5 = site5.substring(0, site5.indexOf(' '));
		
		br.close();
		fr.close();
		
		// Pass ID to constructor for CLI thread to use
		MainThread t = new MainThread(myID, privIP);
		
		CLIThread.IpAddrs[0] = site1;
		CLIThread.IpAddrs[1] = site2;
		CLIThread.IpAddrs[2] = site3;
		CLIThread.IpAddrs[3] = site4;
		CLIThread.IpAddrs[4] = site5;
		
//		System.out.println("CLIThread.IpAddrs[1] = " + CLIThread.IpAddrs[0]);
//		System.out.println("CLIThread.IpAddrs[2] = " + CLIThread.IpAddrs[1]);
//		System.out.println("CLIThread.IpAddrs[3] = " + CLIThread.IpAddrs[2]);
//		System.out.println("CLIThread.IpAddrs[4] = " + CLIThread.IpAddrs[3]);
//		System.out.println("CLIThread.IpAddrs[5] = " + CLIThread.IpAddrs[4]);
	}

}
