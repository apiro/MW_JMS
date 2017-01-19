package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Server{

	public static void main(String[] args) throws IOException {
		Thread requestHandler = new Thread(new RequestHandler());
		requestHandler.start();
		
		Thread tweetHandler = new Thread(new TweetHandler());
		tweetHandler.start();
		
		Thread imageStorer = new Thread(new ImageStorer());
		imageStorer.start();
		
		Thread thumbnailCreator = new Thread(new ThumbnailCreator());
		thumbnailCreator.start();
				
		System.out.println("> The Server is waiting for communication...");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();		
	}
}
