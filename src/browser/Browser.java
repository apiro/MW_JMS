package browser;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import server.ImageStorer;
import server.RequestHandler;
import server.ThumbnailCreator;
import server.TweetHandler;

public class Browser extends Thread{
	
	public boolean control = true;
	
	public void run() {
		HashMap<String, Queue> queues= new HashMap<String, Queue>();
		QueueBrowser[] browsers= new QueueBrowser[4];
		Runnable[] runnable = new Runnable[4];
		
		try {
			JMSContext context = ((ConnectionFactory) getContext().lookup("java:comp/DefaultJMSConnectionFactory")).createContext();
			
			queues.put("tweetQueue", (Queue)getContext().lookup("tweetQueue"));
			queues.put("saveQueue", (Queue)getContext().lookup("saveQueue"));
			queues.put("thumbnailQueue", (Queue)getContext().lookup("thumbnailQueue"));
			queues.put("requestQueue", (Queue)getContext().lookup("requestQueue"));
			
			browsers[0]=(context.createBrowser(queues.get(("tweetQueue"))));
			browsers[1]=(context.createBrowser(queues.get(("saveQueue"))));
			browsers[2]=(context.createBrowser(queues.get(("thumbnailQueue"))));
			browsers[3]=(context.createBrowser(queues.get(("requestQueue"))));
			
			runnable[0]= new TweetHandler("TweetHandler_B");
			runnable[1]=new ImageStorer("ImageStorer_B");
			runnable[2]=new ThumbnailCreator("ThumbnailCreator_B");
			runnable[3]=new RequestHandler("RequestHandler_B");
		
		} catch (NamingException e1) {
			return;
		}
		
		print("starting");
		
		while(control) {
			try {
				Thread.sleep(10000);	//check every 10 seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			} 
			
			//Checks the for queues
			for(int i=0;i<4;i++){
				QueueBrowser qb = browsers[i];
				Enumeration<?> enumeration;
				try {
					enumeration = qb.getEnumeration();
					int count = 0;
					while(enumeration.hasMoreElements()) {
						enumeration.nextElement();
						count++;
					}
					//If there are more than 10 messages in the queue, it will run a new thread 
					//	with the handler for that queue
					if (count > 10){
						Thread t = new Thread(runnable[i]);
						t.start();
					}
				} catch (JMSException e) {
					e.printStackTrace();
				} 
			}
		}
	}

	private Context getContext() throws NamingException {
		
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		
		return new InitialContext(props);
	}
	
	public void print(String s) {
		System.out.println("> Browser > " + s);
	}
}
