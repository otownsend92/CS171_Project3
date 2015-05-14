package proj3;

public class MainThread{
	
	private Thread[] eThread = new Thread[4];
	private Thread[] sThread = new Thread[4];
	
	private Thread cliThread = new Thread();
	
	public MainThread(){
		// Constructor
		cliThread = new CLIThread();
		
		cliThread.start();
	}
	
	public static void main(String[] args){
		MainThread t = new MainThread();
	}
}
