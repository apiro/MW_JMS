package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import browser.Browser;


public class Server{

	public static void main(String[] args) throws IOException {
		
		print("starting");
		
		Thread requestHandler = new Thread(new RequestHandler("RequestHandler"));
		requestHandler.start();
		
		Thread tweetHandler = new Thread(new TweetHandler("TweetHandler"));
		tweetHandler.start();
		
		Thread imageStorer = new Thread(new ImageStorer("ImageStorer"));
		imageStorer.start();
		
		Thread thumbnailCreator = new Thread(new ThumbnailCreator("ThumbnailCreator"));
		thumbnailCreator.start();
		
		Browser browser = new Browser();
		browser.start();
		
		print("waiting for communication");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        
        browser.control=false;
	}
	
	public static void print(String s) {
		System.out.println("> Server > " + s);
	}
	
}
