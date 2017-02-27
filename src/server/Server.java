package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import browser.Browser;


public class Server{

	public static void main(String[] args) throws IOException {
		
		print("starting");
		
		RequestHandler rh = new RequestHandler("RequestHandler");
		Thread requestHandler = new Thread(rh);
		requestHandler.start();
		
		TweetHandler th = new TweetHandler("TweetHandler");
		Thread tweetHandler = new Thread(th);
		tweetHandler.start();
		
		ImageStorer is = new ImageStorer("ImageStorer");
		Thread imageStorer = new Thread(is);
		imageStorer.start();
		
		ThumbnailCreator tc = new ThumbnailCreator("ThumbnailCreator");
		Thread thumbnailCreator = new Thread(tc);
		thumbnailCreator.start();
		
		Browser browser = new Browser();
		browser.start();
		
		print("waiting for communication");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        
        browser.control=false;
        rh.stopListening();
        is.stopListening();
        tc.stopListening();
        th.stopListening();
	}
	
	public static void print(String s) {
		System.out.println("> Server > " + s);
	}
	
}
