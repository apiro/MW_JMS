package server;


public class Server{

	public static void main(String[] args) {
		
		new TweetHandler();
		
		new ImageStorer();
		
		new ThumbnailCreator();
		
		
				
		System.out.println("The Server is waiting for communication...");
		//BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		//bufferedReader.readLine();
		
	}
}
