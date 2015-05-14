package proj3;

public class SiteLocks {
	private int[] sites = {0,0,0,0,0};
	public final static int UNLOCKED = 0;
	public final static int READ = 1;
	public final static int WRITE = 2;
	
	public SiteLocks(){
		// A site has been granted a read lock if its value is 1 and a write lock if its value is 2.
	}
	
	public void setLock(int site, int value){
		sites[site] = value;
	}
	
	public int getLock(int site){
		return sites[site];
	}
}
