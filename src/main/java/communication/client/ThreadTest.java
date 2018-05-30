package communication.client;
public class ThreadTest {
	
	public static void main(String[] args) {
		final ThreadInstance obj = new ThreadInstance();
		for (int i = 0; i < 3; i++) {
			new Thread(new Runnable() {
				public void run() {
					System.out.println(obj.getCountAddOne());
				}
			}).start();
		}

	}
	
}
