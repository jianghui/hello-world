package communication.client;
public class ThreadInstance {
	int count = 0;
	protected int getCountAddOne(){
		count = count + 1;
		return count;
	}

}
