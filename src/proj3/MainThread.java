package proj3;

public class MainThread{
	
	private Thread[] eThread = new Thread[4];
	private Thread[] sThread = new Thread[4];
	
	private Thread cliThread = new Thread();
	
	public MainThread(int id){
		// Constructor
		cliThread = new CLIThread(id);
		
		cliThread.start();
	}
	
	public static void main(String[] args){
		// Read from file and get side ID
		// Pass ID to constructor for CLI thread to use
		int ID = 1;
		
		MainThread t = new MainThread(ID);
	}
}
