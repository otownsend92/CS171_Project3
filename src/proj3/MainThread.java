package proj3;

import java.util.ArrayList;
import java.util.Random;

public class MainThread{
	private Thread cliThread = new Thread();
	private Thread commThread = new Thread();
	ArrayList<Integer> quorum = new ArrayList<Integer>();
	
	public MainThread(int id){
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
		commThread = new CommThread(id, quorum);
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
	
	public static void main(String[] args){
		// Read from file and get side ID
		// Pass ID to constructor for CLI thread to use
		int ID = 1;
		
		MainThread t = new MainThread(ID);
	}
}
